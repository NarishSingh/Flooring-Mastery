package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.service.InvalidProductException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProductDaoImplTest {

    ProductDaoImpl testDao;

    public ProductDaoImplTest() {
    }

    @BeforeEach
    public void setUp() throws IOException {
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = actx.getBean("testProductDao", ProductDaoImpl.class);
    }

    /**
     * Test of readProductByID method, of class ProductDaoImpl.
     */
    @Test
    public void testReadProductByID() throws Exception {
        System.out.println("readProductByID");

        //arrange
        final String testProductKeyCarpet = "Carpet";
        final String testProductKeyLaminate = "Laminate";
        Product testCarpet = null;
        Product testLaminate = null;

        //act
        try {
            testCarpet = testDao.readProductByID(testProductKeyCarpet);
            testLaminate = testDao.readProductByID(testProductKeyLaminate);
        } catch (InvalidProductException | ProductReadException e) {
            fail("Valid Products");
        }

        //assert
        assertEquals(testCarpet.getProductType(), testProductKeyCarpet, "Should've retrieved Carpet");
        assertEquals(testLaminate.getProductType(), testProductKeyLaminate, "Should've retrieved Carpet");
    }

    /**
     * Test of readProductByID method's InvalidProductException, of class
     * ProductDaoImpl.
     */
    @Test
    public void testReadProductByIDInvalidProductFail() throws Exception {
        System.out.println("readProductByID");

        //arrange
        final String testProductMarble = "Marble";
        Product testMarble = null;

        //act and assert
        try {
            testMarble = testDao.readProductByID(testProductMarble);
            fail("invalid product");
        } catch (InvalidProductException e) {
            return; //pass
        } catch (ProductReadException e) {
            fail("Valid product data file");
        }
    }

    /**
     * Test of getValidProducts method, of class ProductDaoImpl.
     */
    @Test
    public void testGetValidProducts() throws Exception {
        System.out.println("getValidProducts");

        //arrange
        final String testProductKeyCarpet = "Carpet";
        final String testProductKeyLaminate = "Laminate";
        Product testCarpet = null;
        Product testLaminate = null;
        List<Product> allProducts = new ArrayList<>();

        //act
        try {
            testCarpet = testDao.readProductByID(testProductKeyCarpet);
            testLaminate = testDao.readProductByID(testProductKeyLaminate);

            allProducts = testDao.getValidProducts();
        } catch (InvalidProductException | ProductReadException e) {
            fail("Valid Products");
        }

        //assert
        assertTrue(allProducts.contains(testCarpet), "List should contain Carpet");
        assertTrue(allProducts.contains(testLaminate), "List should contain Laminate");
    }
}
