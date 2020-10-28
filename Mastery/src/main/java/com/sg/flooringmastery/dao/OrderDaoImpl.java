package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoImpl implements OrderDao {

    private TreeMap<LocalDate, TreeMap<Integer, Order>> orders = new TreeMap<>();
    private String ORDER_DIRECTORY;
    private final String DELIMITER = ",";
    private final String DELIMITER_REPLACEMENT = "::";

    //production
    public OrderDaoImpl() {
        this.ORDER_DIRECTORY = ".\\MasteryFileData\\Orders";
    }

    //testing
    public OrderDaoImpl(String orderDirAsText) {
        this.ORDER_DIRECTORY = orderDirAsText;
    }

    @Override
    public Order addOrder(Order newOrder) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        loadAllOrders();

        if (newOrder.getOrderNum() == 0) {
            throw new InvalidOrderNumberException("Cannot add an order of #0");
        } else if (newOrder.getOrderDate().isBefore(LocalDate.now())) {
            throw new NoOrdersOnDateException("Invalid date for order");
        } else {
            //retrieve inner map or create new one, then add order
            TreeMap<Integer, Order> incomingOrders = orders.get(newOrder.getOrderDate());
            if (incomingOrders == null) {
                incomingOrders = new TreeMap<>();
            }

            incomingOrders.put(newOrder.getOrderNum(), newOrder);

            //add to outer map field and write
            orders.put(newOrder.getOrderDate(), incomingOrders);

            writeAllOrders();

            if (orders.containsValue(incomingOrders)) {
                return newOrder;
            } else {
                throw new OrderPersistenceException("Could not persist order");
            }
        }
    }

    @Override
    public Order removeOrder(LocalDate removalDate, int removalID) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        loadAllOrders();

        //retrieve inner treemap then remove order with exception
        TreeMap<Integer, Order> deletionMap = orders.get(removalDate);
        if (deletionMap == null) {
            throw new NoOrdersOnDateException("No orders on this date to cancel");
        }

        Order deleted = deletionMap.remove(removalID);
        if (deleted == null) {
            throw new InvalidOrderNumberException("No such order exists");
        }

        //re-enter new map to outer treemap and write to file
        orders.put(removalDate, deletionMap);

        writeAllOrders();

        if (orders.containsValue(deletionMap) && !deletionMap.containsValue(deleted)) {
            return deleted;
        } else {
            throw new OrderPersistenceException("Could not persist order deletion");
        }
    }

    @Override
    public Order getOrder(LocalDate date, int id) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        loadAllOrders();

        Map<Integer, Order> orderByDate = orders.get(date);
        if (orderByDate == null) {
            throw new NoOrdersOnDateException("No orders on this date to retrieve");
        }

        Order retrievedOrder = orderByDate.get(id);
        if (retrievedOrder == null) {
            throw new InvalidOrderNumberException("No such order exists");
        }

        return retrievedOrder;
    }

    @Override
    public Order editOrder(Order orderToReplace, Order orderEdit) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException {
        //validate and add since both use .put()
        if (orderEdit.getOrderDate() != orderToReplace.getOrderDate()) {
            throw new NoOrdersOnDateException("Cannot edit order due to date mismatch");
        } else if (orderEdit.getOrderNum() != orderToReplace.getOrderNum()) {
            throw new InvalidOrderNumberException("Could not edit order due to order number mismatch");
        } else {
            return addOrder(orderEdit); //will persist via this method also
        }
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException,
            NoOrdersOnDateException {
        loadAllOrders();

        try {
            return new ArrayList<>(orders.get(date).values());
        } catch (NullPointerException e) {
            throw new NoOrdersOnDateException("No orders to display");
        }
    }

    @Override
    public List<Order> getAllOrders() throws OrderPersistenceException {
        loadAllOrders();

        List<Order> allOrdersList = new ArrayList<>();
        orders.forEach((date, orderTree) -> {
            allOrdersList.addAll(orderTree.values());
        });

        if (allOrdersList.isEmpty()) {
            throw new OrderPersistenceException("No orders retrieved - directory empty");
        } else {
            return allOrdersList;
        }
    }

    @Override
    public int getHighestOrderNumber() throws OrderPersistenceException {
        loadAllOrders();

        //dump all orders to a simple TreeMap and get highest key
        TreeMap<Integer, Order> allOrders = new TreeMap<>();
        orders.forEach((date, orderTree) -> {
            allOrders.putAll(orderTree);
        });

        if (allOrders.isEmpty()) {
            return 0;
        } else {
            return allOrders.lastKey();
        }
    }

    /*DATA (UN)MARSHALLING*/
    /**
     * Retrieve the date from the order filenames
     *
     * @param filename {String} the filename of the orders file for that day,
     *                 contains a date formatted as MMddyyyy
     * @return new LocalDate obj parsed from the filename
     */
    private LocalDate parseDateFromFilename(String filename) {
        String dateString = filename.substring(7, 15); //filename format is Orders_MMddyyyy.txt
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyyy"));
    }

    /**
     * Marshall an Order obj to text
     *
     * @param anOrder {Order} an active order
     * @return {String} a delimited string of text with all obj information
     */
    private String marshallOrder(Order anOrder) {
        String orderAsText = anOrder.getOrderNum() + DELIMITER;

        String convertedName = anOrder.getCustomerName().replace(DELIMITER, DELIMITER_REPLACEMENT);
        orderAsText += convertedName + DELIMITER;

        orderAsText += anOrder.getState().getStateAbbreviation() + DELIMITER;
        orderAsText += anOrder.getState().getTaxRate().toString() + DELIMITER;
        orderAsText += anOrder.getProduct().getProductType() + DELIMITER;
        orderAsText += anOrder.getArea().toString() + DELIMITER;
        orderAsText += anOrder.getProduct().getCostPerSqFt().toString() + DELIMITER;
        orderAsText += anOrder.getProduct().getLaborCostPerSqFt().toString() + DELIMITER;
        orderAsText += anOrder.getMaterialCost().toString() + DELIMITER;
        orderAsText += anOrder.getLaborCost().toString() + DELIMITER;
        orderAsText += anOrder.getTax().toString() + DELIMITER;
        orderAsText += anOrder.getTotal().toString();

        return orderAsText;
    }

    /**
     * Unmarshall delimited text into Order obj's
     *
     * @param orderAsText {String} delimited lines of text in the order
     *                    directory's files
     * @return {Order} a fully constructed Order obj
     */
    private Order unmarshallOrder(String orderAsText, String filename) {
        String[] orderTokens = orderAsText.split(DELIMITER);

        LocalDate orderDate = parseDateFromFilename(filename);

        int orderNum = Integer.parseInt(orderTokens[0]);
        String orderCustName = orderTokens[1].replace(DELIMITER_REPLACEMENT, DELIMITER);

        State orderState = new State(orderTokens[2], new BigDecimal(orderTokens[3]));
        Product orderProduct = new Product(orderTokens[4], new BigDecimal(orderTokens[6]), new BigDecimal(orderTokens[7]));
        BigDecimal orderArea = new BigDecimal(orderTokens[5]);
        BigDecimal orderMatCost = new BigDecimal(orderTokens[8]);
        BigDecimal orderLaborCost = new BigDecimal(orderTokens[9]);
        BigDecimal orderTax = new BigDecimal(orderTokens[10]);
        BigDecimal orderTotal = new BigDecimal(orderTokens[11]);

        return new Order(orderDate, orderNum, orderCustName, orderState, orderProduct,
                orderArea, orderMatCost, orderLaborCost, orderTax, orderTotal);
    }

    /**
     * Load all persisted orders to orders map
     *
     * @throws OrderPersistenceException if cannot read from orders directory or
     *                                   if directory is empty
     */
    private void loadAllOrders() throws OrderPersistenceException {
        File dir = new File(ORDER_DIRECTORY);
        File[] orderDirList = dir.listFiles();

        for (File ordersFile : orderDirList) {
            if (orderDirList.length == 0) {
                break; //nothing to load
            }

            TreeMap<Integer, Order> ordersOnDate = new TreeMap<>(); //inner tree map
            String currentLine;
            Order currentOrder;

            Scanner sc;
            try {
                sc = new Scanner(new BufferedReader(new FileReader(ordersFile)));
            } catch (FileNotFoundException e) {
                throw new OrderPersistenceException("Could not load from Order directory", e);
            }

            ordersOnDate.clear();
            LocalDate ordersDate = parseDateFromFilename(ordersFile.getName());
            sc.nextLine(); //skip header

            while (sc.hasNextLine()) {
                currentLine = sc.nextLine();
                currentOrder = unmarshallOrder(currentLine, ordersFile.getName());

                ordersOnDate.put(currentOrder.getOrderNum(), currentOrder);

            }

            if (ordersOnDate.isEmpty()) {
                continue; //any inner map emptied in last run will be cleaned on load
            } else {
                orders.put(ordersDate, ordersOnDate);
            }

            sc.close();
        }
    }

    /**
     * Persist all active orders to order's directory, or delete an empty orders
     * file
     */
    private void writeAllOrders() throws OrderPersistenceException {
        orders.forEach((date, ordersOnDate) -> {
            //create new file
            String filename = "Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";

            PrintWriter out;
            try {
                File newFile = new File(ORDER_DIRECTORY, filename); //make sure its created in dir
                out = new PrintWriter(new FileWriter(newFile));

                if (ordersOnDate.isEmpty()) {
                    //delete a file if no orders, must close stream first
                    out.close();
                    newFile.delete();
                } else {
                    //header
                    out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                            + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

                    //marshall the orders on date to file
                    ordersOnDate.values().stream()
                            .forEach((order) -> {
                                String orderAsText = marshallOrder(order);
                                out.println(orderAsText);
                                out.flush();
                            });
                }

                out.close();
            } catch (IOException e) {
//              throw new OrderPersistenceException("Could not create order file");
            }
        });
    }
}
