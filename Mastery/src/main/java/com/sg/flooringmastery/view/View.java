package com.sg.flooringmastery.view;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class View {

    private UserIO io;

    public View(UserIO io) {
        this.io = io;
    }

    /**
     * Print main menu and get a user action
     *
     * @return {int} 0-5 corresponding with user's desired menu action
     */
    public int mainMenuAndSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 0. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        boolean hasErrors;
        int selection = 0;

        do {
            try {
                selection = io.readInt("Enter Action: ", 0, 5);
                hasErrors = false;
            } catch (NumberFormatException e) {
                hasErrors = true;
                displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        return selection;
    }

    /*DISPLAY ORDER*/
    /**
     * Display opening Display Order banner to UI
     */
    public void displayDisplayOrderBanner() {
        io.print("===VIEW ORDERS===");
    }

    /**
     * Get a date from user to display orders for
     *
     * @return {LocalDate} a LocalDate obj in MM-dd-yyyy format
     */
    public LocalDate inputDisplayOrderDate() {
        return io.readLocalDate("Enter a valid order's date: ");
    }

    /**
     * Display all orders for a given date
     *
     * @param ordersOnDate {List} all orders on a given date, sorted by order
     *                     number
     */
    public void displayOrdersByDate(List<Order> ordersOnDate) {
        if (ordersOnDate.isEmpty()) {
            io.print("No orders to display");
        } else {
            io.print("----------");
            io.print(ordersOnDate.get(0).getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            io.print("----------");
            ordersOnDate.stream()
                    .forEach((order) -> {
                        io.print("Order Number: " + order.getOrderNum());
                        io.print("Customer Name: " + order.getCustomerName());
                        io.print("State: " + order.getState().getStateAbbreviation());
                        io.print("State Tax Rate: " + order.getState().getTaxRate().toString() + "%");
                        io.print("Product: " + order.getProduct().getProductType());
                        io.print("Cost per Sq. Ft.: $" + order.getProduct().getCostPerSqFt().toString());
                        io.print("Labor Cost per Sq. Ft. $" + order.getProduct().getLaborCostPerSqFt().toString());
                        io.print("Area: " + order.getArea().toString() + " sq. ft.");
                        io.print("Total Material Cost: $" + order.getMaterialCost().toString());
                        io.print("Total Labor Cost: $" + order.getLaborCost().toString());
                        io.print("Total Tax: $" + order.getTax());
                        io.print("Order Total: $" + order.getTotal().toString());
                        io.print("***");
                    });
        }
    }

    /**
     * Display closing Display Order banner for a successful date entry
     */
    public void displayDisplayOrdersSuccessBanner() {
        io.print("***End of orders for this date***");
        io.readString("Press ENTER to continue");
    }

    /**
     * Display closing Display Order banner for a failed date entry
     */
    public void displayDisplayOrdersFailBanner() {
        io.print("***Nothing to display, try again later or with a valid date***");
        io.readString("Press ENTER to continue");
    }

    /*ADD ORDER*/
    /**
     * Display opening Add Order banner to UI
     */
    public void displayAddOrderBanner() {
        io.print("===ADD NEW ORDER===");
    }

    /**
     * Get date from user in MM-dd-yyyy format
     *
     * @return {LocalDate} a future date
     */
    public LocalDate inputOrderAddDate() {
        DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return io.readLocalDate("Enter the order's date (must be later than today, "
                + LocalDate.now().format(mmddyyyy) + ")",
                LocalDate.now().plusDays(1), LocalDate.parse("12-31-9999", mmddyyyy));
    }

    /**
     * Get a customer or company name from user, supports A-Z chars, 0-9, commas
     * and periods
     *
     * @return {String} the customer's name
     */
    public String inputCustomerName() {
        String custName;

        io.print("---");
        do {
            custName = io.readString("Enter customer or company name: ").trim();
        } while (!custName.matches("[a-zA-Z0-9\\,\\.\\s]*"));

        return custName;
    }

    /**
     * Get a state from user
     *
     * @param validStates {List} all valid states read in from file
     * @return {String} the state's abbreviation, formatted to be capitalized
     */
    public String inputState(List<State> validStates) {
        String userState;

        io.print("---");
        io.print("We are available for business in:");
        validStates.stream()
                .forEach((state) -> io.print(state.getStateAbbreviation() + " | " + state.getStateName()));

        do {
            userState = io.readString("Please input your state's abbreviation: ").trim().toUpperCase();
        } while (!userState.matches("[a-zA-Z]{2}"));

        return userState;
    }

    /**
     * Get a product type from user
     *
     * @param validProducts {List} all valid products read in from file
     * @return {String} the product type being ordered
     */
    public String inputProductType(List<Product> validProducts) {
        String rawProduct;

        io.print("---");
        io.print("Our currently available products include (all costs per sq.ft.): ");
        validProducts.stream()
                .forEach((product) -> io.print(product.getProductType()
                + " | Material: $" + product.getCostPerSqFt()
                + " | Labor: $" + product.getLaborCostPerSqFt()));

        do {
            rawProduct = io.readString("Please input your desired product type: ").trim();
        } while (!rawProduct.matches("[a-zA-Z]*"));

        String productSelection = rawProduct.substring(0, 1).toUpperCase()
                + rawProduct.substring(1).toLowerCase(); //format to match map key

        return productSelection;
    }

    /**
     * Get an area from user
     *
     * @return {BigDecimal} an area at or over 100 sq.ft.
     */
    public BigDecimal inputArea() {
        io.print("---");
        return io.readBigDecimal("Please input the area of floor in sq.ft. (minimum 100): ",
                new BigDecimal("100"), BigDecimal.valueOf(Double.MAX_VALUE));
    }

    /**
     * Display all fields of an order object for user
     *
     * @param userOrder {Order} a newly constructed Order obj from service
     */
    public void displayOrderInfo(Order userOrder) {
        io.print("---");
        io.print("Your order request:");
        io.print("Date: " + userOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        io.print("ID: " + userOrder.getOrderNum());
        io.print("Customer Name: " + userOrder.getCustomerName());
        io.print("State: " + userOrder.getState().getStateAbbreviation());
        io.print("Product: " + userOrder.getProduct().getProductType());
        io.print("Area in sq.ft.: " + userOrder.getArea());
        io.print("Material Cost: $" + userOrder.getMaterialCost());
        io.print("Labor Cost: $" + userOrder.getLaborCost());
        io.print("Tax: $" + userOrder.getTax());
        io.print("Total Cost: $" + userOrder.getTotal());
    }

    /**
     * Display the order and get confirmation before writing it to the record
     *
     * @param userOrder {Order} the user's newly create, valid order
     * @return {boolean} confirmation to persist order to file
     */
    public boolean confirmNewOrder(Order userOrder) {
        String userChoice;

        displayOrderInfo(userOrder);

        do {
            userChoice = io.readString("Confirm order placement? (Y - Yes | N - No): ").trim();
        } while (!userChoice.matches("[nyNY]{1}"));

        switch (userChoice) {
            case "y": {
                return true;
            }
            case "Y": {
                return true;
            }
            case "n": {
                return false;
            }
            case "N": {
                return false;
            }
            default: {
                io.print("Unknown command. Order placement terminated.");
                return false;
            }
        }
    }

    /**
     * Display closing Add Order banner for a successful order request
     */
    public void displayAddOrderSuccessBanner() {
        io.print("***New Order Added***");
        io.readString("Press ENTER to continue");
    }

    /**
     * Display closing Add Order banner for a failed order request
     */
    public void displayAddOrderFailBanner() {
        io.print("***Order Request Failed***");
        io.readString("Press ENTER to continue");
    }

    /*EDIT ORDER*/
    /**
     * Display opening Edit Order banner to UI
     */
    public void displayEditOrderBanner() {
        io.print("===EDIT ORDER===");
    }

    /**
     * Enter Order day for retrieval in non-addOrder functions
     *
     * @return {LocalDate} user's date
     */
    public LocalDate inputOrderDate() {
        return io.readLocalDate("Enter the order's date");
    }

    /**
     * Get order number for retrieval or removal
     *
     * @return {int} an existing order ID
     */
    public int inputOrderNumber() {
        boolean hasErrors;
        int orderID = 0;

        do {
            try {
                orderID = io.readInt("Enter Order ID: ");
                hasErrors = false;
            } catch (NumberFormatException e) {
                displayErrorMessage(e.getMessage());
                hasErrors = true;
            }
        } while (hasErrors);

        return orderID;
    }

    /**
     * Edit the name on an order
     *
     * @param orderToEdit {Order} the original order obj, used to fill in
     *                    original value if user elects not to edit by inputting
     *                    \\r
     * @return {String} the new customer name field for the order
     */
    public String inputEditedCustomerName(Order orderToEdit) {
        String newCustName;

        io.print("---");
        io.print("Current name on order: " + orderToEdit.getCustomerName());

        do {
            newCustName = io.readString("Enter new customer or company name, or press ENTER to keep: ").trim();

            if (newCustName.matches("\\r?")) {
                return orderToEdit.getCustomerName();
            }
        } while (!newCustName.matches("[a-zA-Z0-9\\,\\.\\s\\n\\r]*"));

        return newCustName;
    }

    /**
     * Edit the state name of an order
     *
     * @param orderToEdit {Order} the original order obj, used to fill in
     *                    original value if user elects not to edit by inputting
     *                    \\r
     * @param validStates {List} all valid states for business, read in from
     *                    file
     * @return {String} the new State name or abbreviation for the respective
     *         field of the order
     */
    public String inputEditedState(Order orderToEdit, List<State> validStates) {
        String newState;

        io.print("---");
        io.print("Current state on order: " + orderToEdit.getState().getStateAbbreviation());

        io.print("You may change state of transaction to:");
        validStates.stream()
                .forEach((state) -> io.print(state.getStateAbbreviation() + " | " + state.getStateName()));

        do {
            newState = io.readString("Please input the new state's abbreviation, or press ENTER to keep: ").trim().toUpperCase();

            if (newState.matches("\\r?")) {
                return orderToEdit.getState().getStateAbbreviation();
            }
        } while (!newState.matches("[a-zA-Z\\n\\r]{1,2}"));

        return newState;
    }

    /**
     * Edit the product type of an order
     *
     * @param orderToEdit   {Order} the original order obj, used to fill in
     *                      original value if user elects not to edit by
     *                      inputting \\r
     * @param validProducts {List} all valid products for purchase, read in from
     *                      file
     * @return {String} the new product name for the respective field for the
     *         order
     */
    public String inputEditedProductType(Order orderToEdit, List<Product> validProducts) {
        String rawProduct;

        io.print("---");
        io.print("Current product on order: " + orderToEdit.getProduct().getProductType());

        io.print("You may choose from this product selection (all costs per sq.ft.): ");
        validProducts.stream()
                .forEach((product) -> io.print(product.getProductType()
                + " | Material: $" + product.getCostPerSqFt()
                + " | Labor: $" + product.getLaborCostPerSqFt()));

        do {
            rawProduct = io.readString("Please input new product type, or press ENTER to keep: ").trim();

            if (rawProduct.matches("\\r?")) {
                return orderToEdit.getProduct().getProductType();
            }
        } while (!rawProduct.matches("[a-zA-Z\\n\\r]*"));

        String productSelection = rawProduct.substring(0, 1).toUpperCase()
                + rawProduct.substring(1).toLowerCase(); //format to match map key
        return productSelection;
    }

    /**
     * Edit the area of an order
     *
     * @param orderToEdit {Order} the original order obj, used to fill in
     *                    original value if user elects not to edit by inputting
     *                    \\r
     * @return {BigDecimal} new area of at least 100 sq.ft. of the order
     */
    public BigDecimal inputEditedArea(Order orderToEdit) {
        io.print("---");
        io.print("Current area on order: " + orderToEdit.getArea().toString() + " sq.ft.");

        String newAreaString;
        BigDecimal newArea = null;
        boolean hasErrors;

        do {
            do {
                newAreaString = io.readString("Please input new area of floor in sq.ft. (minimum 100),"
                        + " or press ENTER to keep: ");

                if (newAreaString.matches("\\r?")) {
                    return orderToEdit.getArea();
                }
            } while (!newAreaString.matches("[0-9\\n\\r]*"));

            if (new BigDecimal(newAreaString).compareTo(new BigDecimal("100")) < 0) {
                hasErrors = true;
            } else {
                newArea = new BigDecimal(newAreaString);
                hasErrors = false;
            }
        } while (hasErrors);

        return newArea;
    }

    /**
     * Display the edited order and get confirmation before writing to file
     *
     * @param orderEdits {Order} the edited Order
     * @return {boolean} confirmation to persist order edits to file
     */
    public boolean confirmOrderEdit(Order orderEdits) {
        String userChoice;

        displayOrderInfo(orderEdits);

        do {
            userChoice = io.readString("Confirm order edit? (Y - Yes | N - No): ").trim();
        } while (!userChoice.matches("[nyNY]{1}"));

        switch (userChoice) {
            case "y": {
                return true;
            }
            case "Y": {
                return true;
            }
            case "n": {
                return false;
            }
            case "N": {
                return false;
            }
            default: {
                io.print("Unknown command. Order edit terminated.");
                return false;
            }
        }
    }

    /**
     * Display closing Edit Order banner for a successful edit
     */
    public void displayEditOrderSuccessBanner() {
        io.print("***Order Sucessfully Edited***");
        io.readString("Press ENTER to continue");
    }

    /**
     * Display closing Edit Order banner for a failed edit
     */
    public void displayEditOrderFailBanner() {
        io.print("***Order Edit Cancelled***");
        io.readString("Press ENTER to continue");
    }

    /*REMOVE ORDER*/
    /**
     * Display opening Remove Order banner to UI
     */
    public void displayRemoveOrderBanner() {
        io.print("===REMOVE ORDER===");
    }

    /**
     * Get confirmation for removing an active order
     *
     * @param orderToRemove {Order} the order the user wants to remove
     * @return {boolean} confirmation to persist the removal
     */
    public boolean confirmOrderRemoval(Order orderToRemove) {
        String userChoice;
        displayOrderInfo(orderToRemove);

        do {
            userChoice = io.readString("Confirm order cancellation? (Y - Yes | N - No): ").trim();
        } while (!userChoice.matches("[nyNY]{1}"));

        switch (userChoice) {
            case "y": {
                return true;
            }
            case "Y": {
                return true;
            }
            case "n": {
                return false;
            }
            case "N": {
                return false;
            }
            default: {
                io.print("Unknown command. Order cancellation terminated.");
                return false;
            }
        }
    }

    /**
     * Display closing Remove Order banner for a successful edit
     */
    public void displayRemoveOrderSuccessBanner() {
        io.print("***Order Sucessfully Removed***");
        io.readString("Press ENTER to continue");
    }

    /**
     * Display closing Remove Order banner for a failed edit
     */
    public void displayRemoveOrderFailBanner() {
        io.print("***Order Not Removed***");
        io.readString("Press ENTER to continue");
    }

    /*EXPORT ORDER*/
    /**
     * Display opening Export Order banner to UI
     */
    public void displayExportOrderBanner() {
        io.print("===EXPORT ORDERS===");
    }

    /**
     * Display closing Export Order banner for a successful persistence
     */
    public void displayExportOrderSuccessBanner() {
        io.print("***Orders Sucessfully Persisted***");
        io.print("Check DataExport.txt of BackUp Folder for all active orders");
        io.readString("Press ENTER to continue");
    }

    /**
     * Display closing Export Order banner for a failed persistence
     */
    public void displayExportOrderFailBanner() {
        io.print("***Orders Unsucessfully Persisted***");
        io.readString("Press ENTER to continue");
    }

    /*EXIT*/
    /**
     * Display Exit banner in UI
     */
    public void displayExitBanner() {
        io.print("***Thank you***");
    }

    /*EXCEPTION HANDLING*/
    /**
     * Display Unknown Command banner in UI
     */
    public void displayUnknownCommandBanner() {
        io.print("Unknown command");
    }

    /**
     * Display error message for issues with IO
     *
     * @param errorMsg {String} error message to user
     */
    public void displayErrorMessage(String errorMsg) {
        io.print("===Error===");
        io.print(errorMsg);
    }
}
