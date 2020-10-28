package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    /**
     * Add an active order to orders map and persist to file
     *
     * @param newOrder {Order} a valid order obj
     * @return {Order} the successfully added and persisted Order obj
     * @throws OrderPersistenceException   if cannot persist addition
     * @throws NoOrdersOnDateException     if an past date is attempted
     * @throws InvalidOrderNumberException if order num of 0 is attempted
     */
    Order addOrder(Order newOrder) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException;

    /**
     * Remove an active order from orders map and persist to file
     *
     * @param removalDate {LocalDate} a future date which contains at least one
     *                    active order
     * @param removalID   {int} the id of an active order
     * @return {Order} the successfully removed obj
     * @throws OrderPersistenceException   if cannot persist removal
     * @throws NoOrdersOnDateException     if no orders exist on this date
     * @throws InvalidOrderNumberException if order does not exist
     */
    Order removeOrder(LocalDate removalDate, int removalID) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException;

    /**
     * Retrieve an active order
     *
     * @param Date {LocalDate} a future date which contains at least one active
     *             order
     * @param id   {int} the id of an active order
     * @return {Order} an active order
     * @throws OrderPersistenceException   if cannot read from orders directory
     *                                     and/or files
     * @throws NoOrdersOnDateException     if no orders exist on this date
     * @throws InvalidOrderNumberException if order does not exist
     */
    Order getOrder(LocalDate Date, int id) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException;

    /**
     * Edit an active order in the orders map and persist to file
     *
     * @param orderToReplace {Order} a valid order obj to be replaced
     * @param orderEdit      {Order} an order obj with matching date and id
     *                       fields as the other parameter
     * @return {Order} the successfully edited order replacing the original in
     *         the orders map
     * @throws OrderPersistenceException   if cannot persist edit
     * @throws NoOrdersOnDateException     if dates mismatch
     * @throws InvalidOrderNumberException if nums mismatch
     */
    Order editOrder(Order orderToReplace, Order orderEdit) throws OrderPersistenceException,
            NoOrdersOnDateException, InvalidOrderNumberException;

    /**
     * Retrieve the orders for one valid date
     *
     * @param date {LocalDate} a future date that corresponds to an existing key
     *             in the orders map
     * @return {List} list of all active orders for that date
     * @throws OrderPersistenceException if cannot read from orders directory
     * @throws NoOrdersOnDateException   if no orders exist on this date
     */
    List<Order> getOrdersByDate(LocalDate date) throws OrderPersistenceException,
            NoOrdersOnDateException;

    /**
     * Return all active orders
     *
     * @return {List} all active/persisted orders
     * @throws OrderPersistenceException if cannot load from orders directory
     */
    List<Order> getAllOrders() throws OrderPersistenceException;

    /**
     * Find highest key value of all internal order maps
     *
     * @return {int} highest order number thus far
     * @throws OrderPersistenceException if cannot read from orders directory
     */
    int getHighestOrderNumber() throws OrderPersistenceException;
}
