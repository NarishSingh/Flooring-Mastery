package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderDaoImplTest {

    private OrderDao testDao;
    public static Order firstOrder;
    public static Order firstOrderReplacement;
    public static Order secondOrder;
    public static Order thirdOrder;

    public OrderDaoImplTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        final LocalDate testDate = LocalDate.parse("01-01-2021", DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        //first order
        final int testOrderNum = 1;
        final String testCustomerName = "John Doe";
        final State testTexas = new State("TX", new BigDecimal("4.45"));
        final Product testCarpet = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        final BigDecimal testArea100 = new BigDecimal("100");
        final BigDecimal testMatCost = testCarpet.getCostPerSqFt().multiply(testArea100);
        final BigDecimal testLaborCost = testArea100.multiply(testCarpet.getLaborCostPerSqFt());
        final BigDecimal testTax = (testMatCost.add(testLaborCost)).multiply((testTexas.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal testTotal = testMatCost.add(testLaborCost).add(testTax);

        firstOrder = new Order(testDate, testOrderNum, testCustomerName, testTexas, testCarpet, testArea100, testMatCost, testLaborCost, testTax, testTotal);

        //first order replacement
        final String editName = "Juan Dos";
        final State editCali = new State("CA", new BigDecimal("25.00"));
        final Product editLaminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        final BigDecimal editArea200 = new BigDecimal("200");
        final BigDecimal editMatCost = editLaminate.getCostPerSqFt().multiply(editArea200);
        final BigDecimal editLaborCost = editArea200.multiply(editLaminate.getLaborCostPerSqFt());
        final BigDecimal editTax = (editMatCost.add(editLaborCost)).multiply((editCali.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal editTotal = editMatCost.add(editLaborCost).add(editTax);

        firstOrderReplacement = new Order(testDate, testOrderNum, editName, editCali, editLaminate, editArea200, editMatCost, editLaborCost, editTax, editTotal);

        //second order, same date as first but distinct order
        final int nextOrderNum = 2;
        final String nextName = "Juan Dos";
        final State nextCali = new State("CA", new BigDecimal("25.00"));
        final Product nextLaminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        final BigDecimal nextArea200 = new BigDecimal("200");
        final BigDecimal nextMatCost = nextLaminate.getCostPerSqFt().multiply(nextArea200);
        final BigDecimal nextLaborCost = nextArea200.multiply(nextLaminate.getLaborCostPerSqFt());
        final BigDecimal nextTax = (nextMatCost.add(nextLaborCost)).multiply((nextCali.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal nextTotal = nextMatCost.add(nextLaborCost).add(nextTax);

        secondOrder = new Order(testDate, nextOrderNum, nextName, nextCali, nextLaminate, nextArea200, nextMatCost, nextLaborCost, nextTax, nextTotal);

        //third order not on same date as either
        final LocalDate testDate2 = LocalDate.parse("02-02-2021", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        final int thirdOrderNum = 3;
        final String thirdName = "Anthony Third";
        final State thirdCali = new State("CA", new BigDecimal("25.00"));
        final Product thirdLaminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        final BigDecimal thirdArea200 = new BigDecimal("300");
        final BigDecimal thirdMatCost = thirdLaminate.getCostPerSqFt().multiply(thirdArea200);
        final BigDecimal thirdLaborCost = thirdArea200.multiply(thirdLaminate.getLaborCostPerSqFt());
        final BigDecimal thirdTax = (thirdMatCost.add(thirdLaborCost)).multiply((thirdCali.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal thirdTotal = thirdMatCost.add(thirdLaborCost).add(thirdTax);

        thirdOrder = new Order(testDate2, thirdOrderNum, thirdName, thirdCali, thirdLaminate, thirdArea200, thirdMatCost, thirdLaborCost, thirdTax, thirdTotal);
    }

    @BeforeEach
    public void setUp() throws IOException {
        //scrub directory every test
        File testDir = new File(".\\TestingFileData\\Orders");
        for (File file : testDir.listFiles()) {
            file.delete();
        }

        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = actx.getBean("testOrderDao", OrderDaoImpl.class);
    }

    /**
     * Test of addOrder, getOrder, getAllOrders method, of class OrderDaoImpl.
     */
    @Test
    public void testAddGetOrder() throws Exception {
        System.out.println("addOrder");
        //arrange
        //act
        testDao.addOrder(firstOrder);
        Order added = testDao.getOrder(firstOrder.getOrderDate(), firstOrder.getOrderNum());
        List<Order> allOrders = testDao.getAllOrders();

        //assert
        assertEquals(added, firstOrder, "Order should've been added");
        assertTrue(allOrders.contains(added), "Order list should contain added order");
    }

    /**
     * Test of removeOrder method, of class OrderDaoImpl.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        System.out.println("removeOrder");
        //arrange
        //act
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        Order removed = testDao.removeOrder(firstOrder.getOrderDate(), firstOrder.getOrderNum());
        List<Order> allOrders = testDao.getAllOrders();

        //assert
        assertEquals(removed, firstOrder, "Order should've been removed");
        assertFalse(allOrders.contains(removed), "Order list should not contain removed order");
    }

    /**
     * Test of getOrder method's NoOrdersOnDateException, of class OrderDaoImpl.
     */
    @Test
    public void testGetOrderDateFail() throws Exception {
        System.out.println("removeOrder - fail");
        //arrange
        //act & assert
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        testDao.addOrder(thirdOrder);

        try {
            Order noDate1 = testDao.getOrder(LocalDate.now(), firstOrder.getOrderNum());
            Order noDate2 = testDao.getOrder(LocalDate.now(), secondOrder.getOrderNum());
            Order noDate3 = testDao.getOrder(LocalDate.now(), thirdOrder.getOrderNum());
            
            fail("All orders invalid");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (InvalidOrderNumberException e) {
            fail("valid order number");
        }
    }

    /**
     * Test of getOrder method's InvalidOrderNumberException, of class
     * OrderDaoImpl.
     */
    @Test
    public void testGetOrderNumFail() throws Exception {
        System.out.println("removeOrder - fail");
        //arrange
        //act & assert
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        testDao.addOrder(thirdOrder);

        try {
            final int badOrderNum = 99;
            
            Order badNum1 = testDao.getOrder(firstOrder.getOrderDate(), badOrderNum);
            Order badNum2 = testDao.getOrder(secondOrder.getOrderDate(), badOrderNum);
            Order badNum3 = testDao.getOrder(thirdOrder.getOrderDate(), badOrderNum);
            fail("All orders invalid");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (NoOrdersOnDateException e) {
            fail("valid order dates");
        }
    }

    /**
     * Test of editOrder method, of class OrderDaoImpl.
     */
    @Test
    public void testEditOrder() throws Exception {
        System.out.println("editOrder");
        //arrange

        //act
        testDao.addOrder(firstOrder);

        testDao.editOrder(firstOrder, firstOrderReplacement);
        Order edited = testDao.getOrder(firstOrderReplacement.getOrderDate(), firstOrderReplacement.getOrderNum());
        List<Order> allOrders = testDao.getAllOrders();

        //assert
        assertEquals(edited, firstOrderReplacement, "Edited order should be editOrder");
        assertNotEquals(edited, firstOrder, "Edited order should not be originalOrder");
        assertTrue(allOrders.contains(firstOrderReplacement), "List should contain editOrder");
        assertFalse(allOrders.contains(firstOrder), "List should not contain originalOrder");
    }

    /**
     * Test of editOrder method's NoOrdersOnDateException, of class
     * OrderDaoImpl.
     */
    @Test
    public void testEditOrderDateFail() throws Exception {
        System.out.println("editOrder - fail");
        //arrange
        Order firstOrderBadDate = new Order(LocalDate.now(), firstOrder.getCustomerName(),
                firstOrder.getState(), firstOrder.getProduct(), firstOrder.getArea());
        firstOrderBadDate.setOrderNum(firstOrder.getOrderNum());

        //act and assert
        testDao.addOrder(firstOrder);

        try {
            testDao.editOrder(firstOrder, firstOrderBadDate);
            fail("Bad edit");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (InvalidOrderNumberException e) {
            fail("valid order num");
        }

    }

    /**
     * Test of editOrder method's InvalidOrderNumberException, of class
     * OrderDaoImpl.
     */
    @Test
    public void testEditOrderNumFail() throws Exception {
        System.out.println("editOrder - fail");
        //arrange
        Order firstOrderBadNum = new Order(firstOrder.getOrderDate(), firstOrder.getCustomerName(),
                firstOrder.getState(), firstOrder.getProduct(), firstOrder.getArea());
        firstOrderBadNum.setOrderNum(99);

        //act and assert
        testDao.addOrder(firstOrder);

        try {
            testDao.editOrder(firstOrder, firstOrderBadNum);
            fail("Bad edit");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (NoOrdersOnDateException e) {
            fail("valid order date");
        }
    }

    /**
     * Test of getOrdersByDate method, of class OrderDaoImpl.
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        System.out.println("getOrdersByDate");
        //arrange
        //act
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        testDao.addOrder(thirdOrder);
        List<Order> ordersOnDate = testDao.getOrdersByDate(firstOrder.getOrderDate());

        //assert
        assertTrue(ordersOnDate.contains(firstOrder), "List should contain first order");
        assertTrue(ordersOnDate.contains(secondOrder), "List should contain second order");
        assertFalse(ordersOnDate.contains(thirdOrder), "List should not contain third order");
    }

    /**
     * Test of getOrdersByDate method's NoOrdersOnDateException, of class
     * OrderDaoImpl.
     */
    @Test
    public void testGetOrdersByDateFail() throws Exception {
        System.out.println("getOrdersByDate - fail");
        //arrange
        //act and assert
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        testDao.addOrder(thirdOrder);

        try {
            List<Order> ordersOnDate = testDao.getOrdersByDate(LocalDate.now());
            fail("No dates to get");
        } catch (NoOrdersOnDateException e) {
            return;
        }
    }

    /**
     * Test of getAllOrders method, of class OrderDaoImpl.
     */
    @Test
    public void testGetAllOrders() throws Exception {
        //arrange
        //act
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        testDao.addOrder(thirdOrder);
        List<Order> allOrders = testDao.getAllOrders();

        //assert
        assertTrue(allOrders.contains(firstOrder), "List should contain first order");
        assertTrue(allOrders.contains(secondOrder), "List should contain second order");
        assertTrue(allOrders.contains(thirdOrder), "List should contain third order");
    }

    /**
     * Test of getHighestOrderNumber method, of class OrderDaoImpl.
     */
    @Test
    public void testGetHighestOrderNumber() throws Exception {
        //arrange
        //act
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        int maxNum = testDao.getHighestOrderNumber();

        //assert
        assertEquals(maxNum, 2, "Highest order num should be 2");
    }
}
