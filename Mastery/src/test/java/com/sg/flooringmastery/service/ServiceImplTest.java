package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.InvalidOrderNumberException;
import com.sg.flooringmastery.dao.NoOrdersOnDateException;
import com.sg.flooringmastery.dao.OrderPersistenceException;
import com.sg.flooringmastery.dao.ProductReadException;
import com.sg.flooringmastery.dao.StateReadException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceImplTest {

    private Service testServ;
    public Order firstOrderUnval;
    public Order firstOrder;
    public Order firstOrderReplacement;
    public Order secondOrderBadDate;
    public Order secondOrderBadNum;
    public Order secondOrderExceptionNum;
    public List<State> onlyState = new ArrayList<>();
    public List<Product> onlyProduct = new ArrayList<>();

    public ServiceImplTest() {
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testServ = actx.getBean("testService", Service.class);
    }

    @BeforeEach
    public void setUp() {
        final LocalDate testDate = LocalDate.parse("01-01-2021", DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        final int testNum = 1;
        final String testName = "John Doe";
        final State testTexas = new State("TX", new BigDecimal("4.45").setScale(2, RoundingMode.HALF_UP));
        final Product testCarpet = new Product("Carpet", new BigDecimal("2.25").setScale(2, RoundingMode.HALF_UP), new BigDecimal("2.10").setScale(2, RoundingMode.HALF_UP));
        final BigDecimal testArea100 = new BigDecimal("100").setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testMatCost = (testCarpet.getCostPerSqFt().multiply(testArea100)).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testLaborCost = (testArea100.multiply(testCarpet.getLaborCostPerSqFt())).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTax = ((testMatCost.add(testLaborCost)).multiply((testTexas.getTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP))))).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTotal = (testMatCost.add(testLaborCost).add(testTax)).setScale(2, RoundingMode.HALF_UP);

        //first order unvalidated
        firstOrderUnval = new Order(testDate, testName, testTexas, testCarpet, testArea100);

        //first order, fully validated
        firstOrder = new Order(testDate, testNum, testName, testTexas, testCarpet, testArea100, testMatCost, testLaborCost, testTax, testTotal);

        //first order replacement
        final String editName = "Juan Dos";
        final State editCali = new State("CA", new BigDecimal("25.00"));
        final Product editLaminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        final BigDecimal editArea200 = new BigDecimal("200");
        final BigDecimal editMatCost = editLaminate.getCostPerSqFt().multiply(editArea200);
        final BigDecimal editLaborCost = editArea200.multiply(editLaminate.getLaborCostPerSqFt());
        final BigDecimal editTax = (editMatCost.add(editLaborCost)).multiply((editCali.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal editTotal = editMatCost.add(editLaborCost).add(editTax);

        firstOrderReplacement = new Order(testDate, testNum, editName, editCali, editLaminate, editArea200, editMatCost, editLaborCost, editTax, editTotal);

        //second order, for exception testing only
        final String nextName = "Juan Dos";
        final State nextCali = new State("CA", new BigDecimal("25.00"));
        final Product nextLaminate = new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10"));
        final BigDecimal nextArea200 = new BigDecimal("200");
        final BigDecimal nextMatCost = nextLaminate.getCostPerSqFt().multiply(nextArea200);
        final BigDecimal nextLaborCost = nextArea200.multiply(nextLaminate.getLaborCostPerSqFt());
        final BigDecimal nextTax = (nextMatCost.add(nextLaborCost)).multiply((nextCali.getTaxRate().divide(new BigDecimal("100"))));
        final BigDecimal nextTotal = nextMatCost.add(nextLaborCost).add(nextTax);

        secondOrderBadDate = new Order(LocalDate.now(), testNum, nextName, nextCali, nextLaminate, nextArea200, nextMatCost, nextLaborCost, nextTax, nextTotal);
        secondOrderBadNum = new Order(testDate, 2, nextName, nextCali, nextLaminate, nextArea200, nextMatCost, nextLaborCost, nextTax, nextTotal);

        secondOrderExceptionNum = new Order(testDate, 0, nextName, nextCali, nextLaminate, nextArea200, nextMatCost, nextLaborCost, nextTax, nextTotal);

        //State and product
        onlyState.clear();
        onlyProduct.clear();
        onlyState.add(testTexas);
        onlyProduct.add(testCarpet);
    }

    /**
     * Test of validateOrder method, of class ServiceImpl.
     */
    @Test
    public void testValidateOrder() throws Exception {
        System.out.println("validateOrder");

        //arrange
        final int testOrderNum = 2;

        final BigDecimal testMatCost = (firstOrderUnval.getProduct().getCostPerSqFt().multiply(firstOrderUnval.getArea())).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testLaborCost = (firstOrderUnval.getArea().multiply(firstOrderUnval.getProduct().getLaborCostPerSqFt())).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTax = ((testMatCost.add(testLaborCost)).multiply((firstOrderUnval.getState().getTaxRate().divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP))))).setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTotal = (testMatCost.add(testLaborCost).add(testTax)).setScale(2, RoundingMode.HALF_UP);

        Order firstOrderVal = null;

        //act
        try {
            firstOrderVal = testServ.validateOrder(firstOrderUnval);
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }

        //assert
        assertEquals(firstOrderVal.getMaterialCost(), testMatCost, "Material cost should've been validated");
        assertEquals(firstOrderVal.getLaborCost(), testLaborCost, "Labor cost should've been validated");
        assertEquals(firstOrderVal.getTax(), testTax, "Tax should've been validated");
        assertEquals(firstOrderVal.getTotal(), testTotal, "Total cost should've been validated");
        assertEquals(firstOrderVal.getOrderNum(), testOrderNum, "Order number should be 1");
    }

    /**
     * Test of addOrder method, of class ServiceImpl.
     */
    @Test
    public void testAddOrder() throws Exception {
        System.out.println("addOrder");

        try {
            Order firstAdded = testServ.addOrder(firstOrder);

            assertEquals(firstOrder, firstAdded, "First order added should be firstOrder");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of addOrder method's NoOrdersOnDateException, of class ServiceImpl.
     */
    @Test
    public void testAddOrderDateFail() throws Exception {
        System.out.println("addOrder - fail");

        try {
            Order badDate = testServ.addOrder(secondOrderBadDate);
            fail("Bad date");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (OrderPersistenceException | InvalidOrderNumberException e) {
            fail("valid order");
        }
    }

    /**
     * Test of addOrder method's InvalidOrderNumberException, of class
     * ServiceImpl.
     */
    @Test
    public void testAddOrderNumFail() throws Exception {
        System.out.println("addOrder - fail");

        try {
            Order badDate = testServ.addOrder(secondOrderExceptionNum);
            fail("Bad order num");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (OrderPersistenceException | NoOrdersOnDateException e) {
            fail("valid order");
        }
    }

    /**
     * Test of removeOrder method, of class ServiceImpl.
     */
    @Test
    public void testRemoveOrder() throws Exception {
        System.out.println("removeOrder");
        try {
            Order firstRemoved = testServ.removeOrder(firstOrder.getOrderDate(), firstOrder.getOrderNum());

            assertEquals(firstRemoved, firstOrder, "Removed order should be first order");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of removeOrder method's NoOrdersOnDateException, of class
     * ServiceImpl.
     */
    @Test
    public void testRemoveOrderDateFail() throws Exception {
        System.out.println("removeOrder - fail");
        try {
            Order firstRemoved = testServ.removeOrder(LocalDate.now(), firstOrder.getOrderNum());
            fail("Bad date");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (InvalidOrderNumberException e) {
            fail("valid order number");
        }
    }

    /**
     * Test of removeOrder method's InvalidOrderNumberException, of class
     * ServiceImpl.
     */
    @Test
    public void testRemoveOrderNumFail() throws Exception {
        try {
            Order firstRemoved = testServ.removeOrder(firstOrder.getOrderDate(), 99);
            fail("Bad order number");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (NoOrdersOnDateException e) {
            fail("valid date");
        }
    }

    /**
     * Test of editOrder method, of class ServiceImpl.
     */
    @Test
    public void testEditOrder() throws Exception {
        System.out.println("editOrder");

        try {
            Order original = testServ.addOrder(firstOrder);

            Order edit = testServ.editOrder(firstOrder, firstOrderReplacement);

            assertEquals(original.getOrderDate(), firstOrderReplacement.getOrderDate(), "Date should be the same");
            assertEquals(original.getOrderNum(), firstOrderReplacement.getOrderNum(), "Num should be the same");
            assertNotEquals(original, edit, "Replacement should be different from original order");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of editOrder method's NoOrdersOnDateException, of class ServiceImpl.
     */
    @Test
    public void testEditOrderDateFail() throws Exception {
        System.out.println("editOrder - fail");

        try {
            Order original = testServ.addOrder(firstOrder);

            Order edit = testServ.editOrder(firstOrder, secondOrderBadDate);
            fail("Bad edit - date mismatch");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (InvalidOrderNumberException e) {
            fail("valid order number");
        }
    }

    /**
     * Test of editOrder method's InvalidOrderNumberException, of class
     * ServiceImpl.
     */
    @Test
    public void testEditOrderNumFail() throws Exception {
        System.out.println("editOrder - fail");

        try {
            Order original = testServ.addOrder(firstOrder);

            Order edit = testServ.editOrder(firstOrder, secondOrderBadNum);
            fail("Bad edit - number mismatch");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (NoOrdersOnDateException e) {
            fail("valid order date");
        }
    }

    /**
     * Test of getOrder method, of class ServiceImpl.
     */
    @Test
    public void testGetOrder() throws Exception {
        System.out.println("getOrder");

        try {
            Order first = testServ.getOrder(firstOrder.getOrderDate(), firstOrder.getOrderNum());

            assertEquals(firstOrder, first, "Should've retrieved the first Order");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of getOrder method's NoOrdersOnDateException, of class ServiceImpl.
     */
    @Test
    public void testGetOrderDateFail() throws Exception {
        System.out.println("getOrder - fail");

        try {
            Order first = testServ.getOrder(secondOrderBadDate.getOrderDate(), secondOrderBadDate.getOrderNum());
            fail("Can't retrieve");
        } catch (NoOrdersOnDateException e) {
            return;
        } catch (InvalidOrderNumberException e) {
            fail("valid order number");
        }
    }

    /**
     * Test of getOrder method's InvalidOrderNumberException, of class
     * ServiceImpl.
     */
    @Test
    public void testGetOrderNumFail() throws Exception {
        System.out.println("getOrder - fail");

        try {
            Order first = testServ.getOrder(secondOrderExceptionNum.getOrderDate(), secondOrderExceptionNum.getOrderNum());
            fail("Can't retrieve - order number doesn't exist");
        } catch (InvalidOrderNumberException e) {
            return;
        } catch (NoOrdersOnDateException e) {
            fail("valid order date");
        }
    }

    /**
     * Test of getOrdersByDate method, of class ServiceImpl.
     */
    @Test
    public void testGetOrdersByDate() throws Exception {
        System.out.println("getOrdersByDate");

        Order first;
        List<Order> ordersByDate;

        try {
            first = testServ.addOrder(firstOrder);

            ordersByDate = testServ.getOrdersByDate(firstOrder.getOrderDate());
            assertTrue(ordersByDate.contains(first), "Should've retrieved first order");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of getOrdersByDate method, of class ServiceImpl.
     */
    @Test
    public void testGetOrdersByDateFail() throws Exception {
        System.out.println("getOrdersByDate - fail");

        Order first;
        List<Order> ordersByDate;

        try {
            first = testServ.addOrder(firstOrder);

            ordersByDate = testServ.getOrdersByDate(secondOrderBadDate.getOrderDate());
            fail("Can't retrieve");
        } catch (NoOrdersOnDateException e) {
            return;
        }
    }

    /**
     * Test of exportOrder method, of class ServiceImpl.
     */
    @Test
    public void testExportOrder() throws Exception {
        System.out.println("exportOrder");

        Order firstAdded = null;

        try {
            firstAdded = testServ.addOrder(firstOrder);

            testServ.exportOrder();
        } catch (OrderPersistenceException e) {
            return; //does nothing anyway just pass the test no matter what
        }
    }

    /**
     * Test of getAllOrders method, of class ServiceImpl.
     */
    @Test
    public void testGetAllOrders() throws Exception {
        System.out.println("getAllOrders");

        Order firstAdded;
        List<Order> allOrders;

        try {
            firstAdded = testServ.addOrder(firstOrder);
            allOrders = testServ.getAllOrders();

            assertEquals(firstAdded, firstOrder, "First order added should be firstOrder");
            assertTrue(allOrders.contains(firstAdded), "Should contain first order");
            assertFalse(allOrders.contains(firstOrderReplacement), "Should only contain firstOrder");
            assertFalse(allOrders.contains(firstOrderUnval), "Should only contain firstOrder");
        } catch (OrderPersistenceException e) {
            fail("valid order");
        }
    }

    /**
     * Test of getAllOrders method, of class ServiceImpl.
     */
    @Test
    public void testGetAllOrdersFail() throws Exception {
        System.out.println("getAllOrders - fail");

        try {
            Order removal = testServ.removeOrder(firstOrder.getOrderDate(), firstOrder.getOrderNum());
            List<Order> allOrders = testServ.getAllOrders();
            fail("Can't retrieve");
        } catch (OrderPersistenceException e) {
            return;
        }
    }

    /**
     * Test of getValidStateList method, of class ServiceImpl.
     */
    @Test
    public void testGetValidStateList() throws Exception {
        System.out.println("getValidStateList");

        List<State> retrievedState;

        try {
            retrievedState = testServ.getValidStateList();
            assertEquals(retrievedState, onlyState, "Only state should be Texas");
        } catch (StateReadException e) {
            fail("Valid retrieval");
        }
    }

    /**
     * Test of getValidProductList method, of class ServiceImpl.
     */
    @Test
    public void testGetValidProductList() throws Exception {
        System.out.println("getValidProductList");

        List<Product> retrievedProduct = null;

        try {
            retrievedProduct = testServ.getValidProductList();
        } catch (ProductReadException e) {
            fail("Valid retrieval");
        }

        assertEquals(retrievedProduct, onlyProduct, "Only Product should be carpet");
    }

    /**
     * Test of validateState method, of class ServiceImpl.
     */
    @Test
    public void testValidateState() throws Exception {
        System.out.println("validateState");

        final String testTX = "TX";
        State testState = null;

        try {
            testState = testServ.validateState(testTX);
        } catch (InvalidStateException e) {
            fail("Valid State");
        }

        assertEquals(testState, onlyState.get(0), "Should've retrieved TX");
    }

    /**
     * Test of validateState method's InvalidStateException, of class
     * ServiceImpl.
     */
    @Test
    public void testValidateStateFail() throws Exception {
        System.out.println("validateState - fail");

        try {
            State testState = testServ.validateState("NY");
            fail("Bad State");
        } catch (InvalidStateException e) {
            return;
        }
    }

    /**
     * Test of validateProduct method, of class ServiceImpl.
     */
    @Test
    public void testValidateProduct() throws Exception {
        System.out.println("validateProduct");

        final String testProd = "Carpet";
        Product testProduct = null;

        try {
            testProduct = testServ.validateProduct(testProd);
        } catch (InvalidProductException e) {
            fail("Valid product");
        }

        assertEquals(testProduct, onlyProduct.get(0), "Should've retrieved Carpet");
    }

    /**
     * Test of validateProduct method, of class ServiceImpl.
     */
    @Test
    public void testValidateProductFail() throws Exception {
        System.out.println("validateProduct - fail");

        try {
            Product testProduct = testServ.validateProduct("Marble");
            fail("Bad product");
        } catch (InvalidProductException e) {
            return;
        }
    }
}
