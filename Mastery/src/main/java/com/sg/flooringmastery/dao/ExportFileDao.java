/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.util.List;

/**
 *
 * @author naris
 */
public interface ExportFileDao {

    /**
     * Write all active orders to an export file
     *
     * @param activeOrders {List} all active orders
     * @throws OrderPersistenceException if cannot persist active orders to
     *                                   backup directory file
     */
    void exportOrders(List<Order> activeOrders) throws OrderPersistenceException;
}
