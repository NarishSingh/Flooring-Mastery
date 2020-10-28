package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.time.LocalDate;
import java.util.List;
import com.sg.flooringmastery.dao.AuditDao;
import com.sg.flooringmastery.dao.ExportFileDao;
import com.sg.flooringmastery.dao.InvalidOrderNumberException;
import com.sg.flooringmastery.dao.NoOrdersOnDateException;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.ProductReadException;
import com.sg.flooringmastery.dao.StateDao;
import com.sg.flooringmastery.dao.StateReadException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ServiceImpl implements Service {

    private StateDao state;
    private ProductDao product;
    private OrderDao dao;
    private AuditDao audit;
    private ExportFileDao export;

    public ServiceImpl(StateDao state, ProductDao product, OrderDao dao, AuditDao audit, ExportFileDao export) {
        this.state = state;
        this.product = product;
        this.dao = dao;
        this.audit = audit;
        this.export = export;
    }

    @Override
    public Order validateOrder(Order orderRequest) throws OrderPersistenceException {
        calculateOrderCosts(orderRequest);

        int newOrderNum;
        if (orderRequest.getOrderNum() == 0) {
            //new order request will initialize to 0, generate a new order number
            newOrderNum = generateOrderNumber();
        } else {
            newOrderNum = orderRequest.getOrderNum(); //edit order request will already have an order number
        }

        return new Order(orderRequest.getOrderDate(), newOrderNum, orderRequest.getCustomerName(),
                orderRequest.getState(), orderRequest.getProduct(), orderRequest.getArea(),
                orderRequest.getMaterialCost(), orderRequest.getLaborCost(), orderRequest.getTax(),
                orderRequest.getTotal());
    }

    @Override
    public Order addOrder(Order newOrder) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        try {
            return dao.addOrder(newOrder);
        } catch (NoOrdersOnDateException e) {
            throw new NoOrdersOnDateException("Invalid date", e);
        } catch (InvalidOrderNumberException e) {
            throw new InvalidOrderNumberException("Invalid order num", e);
        }
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNum) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        try {
            return dao.removeOrder(date, orderNum);
        } catch (NoOrdersOnDateException e) {
            throw new NoOrdersOnDateException("No orders on date to cancel", e);
        } catch (InvalidOrderNumberException e) {
            throw new InvalidOrderNumberException("Order of this number does not exist", e);
        }
    }

    @Override
    public Order editOrder(Order editedOrder, Order originalOrder) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        try {
            return dao.editOrder(originalOrder, editedOrder);
        } catch (NoOrdersOnDateException e) {
            throw new NoOrdersOnDateException("Invalid date - mismatch", e);
        } catch (InvalidOrderNumberException e) {
            throw new InvalidOrderNumberException("Invalid number - mismatch", e);
        }
    }

    @Override
    public Order getOrder(LocalDate date, int orderNum) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        try {
            return dao.getOrder(date, orderNum);
        } catch (NoOrdersOnDateException e) {
            throw new NoOrdersOnDateException("Invalid date - no orders to retrieve", e);
        } catch (InvalidOrderNumberException e) {
            throw new InvalidOrderNumberException("Invalid order num - order does not exist", e);
        }
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException,
            NoOrdersOnDateException {
        try {
            return dao.getOrdersByDate(date);
        } catch (NoOrdersOnDateException | OrderPersistenceException e) {
            throw new NoOrdersOnDateException("No orders to display", e);
        }
    }

    @Override
    public void exportOrder() throws OrderPersistenceException {
        try {
            List<Order> allActiveOrders = getAllOrders();
            export.exportOrders(allActiveOrders);
        } catch (OrderPersistenceException e) {
            throw new OrderPersistenceException("Could not export order directory", e);
        }
    }

    @Override
    public List<Order> getAllOrders() throws OrderPersistenceException {
        try {
            return dao.getAllOrders();
        } catch (OrderPersistenceException e) {
            throw new OrderPersistenceException("Could not list order directory", e);
        }
    }

    @Override
    public List<State> getValidStateList() throws StateReadException {
        try {
            return state.getValidStates();
        } catch (StateReadException e) {
            throw new StateReadException("Could not load State tax data roster", e);
        }
    }

    @Override
    public List<Product> getValidProductList() throws ProductReadException {
        try {
            return product.getValidProducts();
        } catch (ProductReadException e) {
            throw new ProductReadException("Could not load Product data roster", e);
        }
    }

    @Override
    public State validateState(String userState) throws InvalidStateException {
        try {
            return state.readStateByID(userState);
        } catch (InvalidStateException | StateReadException e) {
            throw new InvalidStateException("We are unavailable for business in this state for now", e);
        }
    }

    @Override
    public Product validateProduct(String userProduct) throws InvalidProductException {
        try {
            return product.readProductByID(userProduct);
        } catch (InvalidProductException | ProductReadException e) {
            throw new InvalidProductException("We do not floor with this type of material at this time", e);
        }
    }

    /*HELPER METHODS*/
    /**
     * Calculate and set the costs for the remaining fields of a order obj
     *
     * @param newOrder {Order} a valid but incomplete order obj request
     */
    private void calculateOrderCosts(Order newOrder) {
        BigDecimal matCosts = newOrder.getProduct().getCostPerSqFt().multiply(newOrder.getArea());
        BigDecimal matCostsScaled = matCosts.setScale(2, RoundingMode.HALF_UP);
        BigDecimal laborCosts = newOrder.getArea().multiply(newOrder.getProduct().getLaborCostPerSqFt());
        BigDecimal laborCostsScaled = laborCosts.setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxCosts = matCosts.add(laborCosts).multiply(newOrder.getState().getTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP)));
        BigDecimal taxCostsScaled = taxCosts.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCosts = matCosts.add(laborCosts).add(taxCosts);
        BigDecimal totalCostsScaled = totalCosts.setScale(2, RoundingMode.HALF_UP);

        newOrder.setMaterialCost(matCostsScaled);
        newOrder.setLaborCost(laborCostsScaled);
        newOrder.setTax(taxCostsScaled);
        newOrder.setTotal(totalCostsScaled);
    }

    /**
     * Find the highest order number so far, and add 1 to set the next order
     * request's number
     *
     * @return {int} a number > 0
     */
    private int generateOrderNumber() throws OrderPersistenceException {
        return dao.getHighestOrderNumber() + 1;
    }
}
