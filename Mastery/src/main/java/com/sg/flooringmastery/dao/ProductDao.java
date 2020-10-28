package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.service.InvalidProductException;
import java.util.List;

public interface ProductDao {

    /**
     * Return all valid products from map as a list
     *
     * @return {List} all valid products
     * @throws ProductReadException if cannot read from product data file
     */
    List<Product> getValidProducts() throws ProductReadException;

    /**
     * Validate the user's product request for a new or edited order
     *
     * @param userProduct {String} user's inputted product name
     * @return {State} the proper product obj corresponding to user's request
     * @throws InvalidProductException if user inputs an invalid product
     *                                 selection
     * @throws ProductReadException    if cannot read from product data file
     */
    Product readProductByID(String userProduct) throws InvalidProductException, ProductReadException;

}
