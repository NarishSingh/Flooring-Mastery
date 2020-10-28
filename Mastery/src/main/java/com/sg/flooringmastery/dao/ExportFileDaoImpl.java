package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportFileDaoImpl implements ExportFileDao {

    public final String BACKUP_FILE;
    public final String DELIMITER = ",";

    public ExportFileDaoImpl() {
        this.BACKUP_FILE = ".\\MasteryFileData\\Backup\\DataExport.txt";
    }

    public ExportFileDaoImpl(String exportFileAsText) {
        this.BACKUP_FILE = exportFileAsText;
    }

    /**
     * Write all active orders to an export file
     *
     * @param activeOrders {List} all active orders
     */
    @Override
    public void exportOrders(List<Order> activeOrders) throws OrderPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(new File(BACKUP_FILE)));

            //header
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");

            //marshall orders
            activeOrders.stream()
                    .forEach((order) -> {
                        String orderAsExportText = marshallOrder(order);
                        out.println(orderAsExportText);
                        out.flush();
                    });

            out.close();
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not export to backup directory");
        }
    }

    /**
     * Marshall an order to delimited String of text
     *
     * @param anOrder {Order} an active order
     * @return {String} the order obj as a delimited String for the export file
     */
    private String marshallOrder(Order anOrder) {
        String orderAsExportText = anOrder.getOrderNum() + DELIMITER;
        orderAsExportText += anOrder.getCustomerName() + DELIMITER;
        orderAsExportText += anOrder.getState().getStateAbbreviation() + DELIMITER;
        orderAsExportText += anOrder.getState().getTaxRate() + DELIMITER;
        orderAsExportText += anOrder.getProduct().getProductType() + DELIMITER;
        orderAsExportText += anOrder.getArea() + DELIMITER;
        orderAsExportText += anOrder.getProduct().getCostPerSqFt() + DELIMITER;
        orderAsExportText += anOrder.getProduct().getLaborCostPerSqFt() + DELIMITER;
        orderAsExportText += anOrder.getMaterialCost() + DELIMITER;
        orderAsExportText += anOrder.getLaborCost() + DELIMITER;
        orderAsExportText += anOrder.getTax() + DELIMITER;
        orderAsExportText += anOrder.getTotal() + DELIMITER;
        orderAsExportText += anOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        return orderAsExportText;
    }

}
