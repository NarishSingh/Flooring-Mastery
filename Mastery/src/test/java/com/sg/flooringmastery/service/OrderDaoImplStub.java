package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.InvalidOrderNumberException;
import com.sg.flooringmastery.dao.NoOrdersOnDateException;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class OrderDaoImplStub implements OrderDao {

    public Order onlyOrder;
    public TreeMap<Integer, Order> oneOrderMap = new TreeMap<>();

    public OrderDaoImplStub() {
        final LocalDate testDate = LocalDate.parse("01-01-2021", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        final int testNum = 1;
        final String testName = "John Doe";
        final State testTexas = new State("TX", new BigDecimal("4.45").setScale(2, RoundingMode.HALF_UP));
        final Product testCarpet = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10").setScale(2, RoundingMode.HALF_UP));
        final BigDecimal testArea100 = new BigDecimal("100").setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testMatCost = (testCarpet.getCostPerSqFt().multiply(testArea100)).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testLaborCost = (testArea100.multiply(testCarpet.getLaborCostPerSqFt())).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTax = ((testMatCost.add(testLaborCost)).multiply((testTexas.getTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP))))).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTotal = (testMatCost.add(testLaborCost).add(testTax)).setScale(2, RoundingMode.HALF_UP);

        this.onlyOrder = new Order(testDate, testNum, testName, testTexas, testCarpet, testArea100, testMatCost, testLaborCost, testTax, testTotal);

        this.oneOrderMap.put(testNum, onlyOrder);
    }

    public OrderDaoImplStub(Order onlyOrder) {
        this.onlyOrder = onlyOrder;
    }

    @Override
    public Order addOrder(Order newOrder) throws OrderPersistenceException, NoOrdersOnDateException, InvalidOrderNumberException {
        if (newOrder.equals(onlyOrder) && newOrder.getOrderDate().isAfter(LocalDate.now())) {
            return oneOrderMap.put(newOrder.getOrderNum(), newOrder);
        } else if (newOrder.getOrderNum() == 0) {
            throw new InvalidOrderNumberException("Cannot add order number of 0");
        } else if (newOrder.getOrderDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new NoOrdersOnDateException("Bad date for add - must be in future");
        } else {
            return null;
        }
    }

    @Override
    public Order removeOrder(LocalDate removalDate, int removalID) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        if (!removalDate.equals(onlyOrder.getOrderDate())) {
            throw new NoOrdersOnDateException("No order to remove");
        } else if (removalID != onlyOrder.getOrderNum()) {
            throw new InvalidOrderNumberException("Invalid order number");
        } else {
            return oneOrderMap.remove(onlyOrder.getOrderNum());
        }
    }

    @Override
    public Order getOrder(LocalDate date, int orderNum) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        if (!date.equals(onlyOrder.getOrderDate())) {
            throw new NoOrdersOnDateException("No order to retrieve");
        } else if (orderNum != onlyOrder.getOrderNum()) {
            throw new InvalidOrderNumberException("Invalid order number");
        } else {
            return oneOrderMap.get(orderNum);
        }
    }

    @Override
    public Order editOrder(Order orderToReplace, Order orderEdit) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        if (orderToReplace.equals(orderEdit)) {
            return oneOrderMap.put(orderToReplace.getOrderNum(), orderEdit);
        } else if (orderEdit.getOrderDate().isBefore(LocalDate.now().plusDays(1))
                || orderEdit.getOrderDate() != orderToReplace.getOrderDate()) {
            throw new NoOrdersOnDateException("Past or mismatched date");
        } else if (orderEdit.getOrderNum() == 0
                || orderEdit.getOrderNum() != orderToReplace.getOrderNum()) {
            throw new InvalidOrderNumberException("0 or mismatched order number");
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException,
            NoOrdersOnDateException {
        if (date.equals(onlyOrder.getOrderDate())) {
            List<Order> orderList = new ArrayList<>();
            orderList.add(oneOrderMap.get(onlyOrder.getOrderNum()));
            return orderList;
        } else {
            throw new NoOrdersOnDateException("No orders to retrieve");
        }
    }

    @Override
    public List<Order> getAllOrders() throws OrderPersistenceException {
        if (oneOrderMap.isEmpty()) {
            throw new OrderPersistenceException("nothing to retrieve");
        } else {
            return new ArrayList<>(oneOrderMap.values());
        }
    }

    @Override
    public int getHighestOrderNumber() throws OrderPersistenceException {
        return 1;
    }

}
