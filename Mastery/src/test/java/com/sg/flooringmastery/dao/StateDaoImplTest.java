package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.State;
import com.sg.flooringmastery.service.InvalidStateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StateDaoImplTest {

    StateDaoImpl testDao;

    public StateDaoImplTest() {
    }

    @BeforeEach
    public void setUp() throws IOException {
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = actx.getBean("testStateDao", StateDaoImpl.class);
    }

    /**
     * Test of readStateByID method, of class StateDaoImpl.
     */
    @Test
    public void testReadStateByID() throws Exception {
        System.out.println("readStateByID");

        //arrange
        final String testStateKeyTX = "TX";
        final String testStateKeyWA = "WA";
        State testTX = null;
        State testWA = null;

        //act
        try {
            testTX = testDao.readStateByID(testStateKeyTX);
            testWA = testDao.readStateByID(testStateKeyWA);
        } catch (InvalidStateException | StateReadException e) {
            fail("Valid states");
        }

        //assert
        assertEquals(testTX.getStateAbbreviation(), testStateKeyTX, "Should've retrieved Texas");
        assertEquals(testWA.getStateAbbreviation(), testStateKeyWA, "Should've retrieved Washington");
    }

    /**
     * Test of readStateByID method's InvalidStateException, of class
     * StateDaoImpl.
     */
    @Test
    public void testReadStateByIDInvalidStateFail() throws Exception {
        System.out.println("readStateByID");

        //arrange
        final String testStateKeyNY = "NY";
        State testCA = null;

        //act and assert
        try {
            testCA = testDao.readStateByID(testStateKeyNY);
            fail("State does not exist");
        } catch (InvalidStateException e) {
            return; //pass
        } catch (StateReadException e) {
            fail("Valid state data file");
        }
    }

    /**
     * Test of getValidStates method, of class StateDaoImpl.
     */
    @Test
    public void testGetValidStates() throws Exception {
        System.out.println("getValidStates");

        //arrange
        final String testStateKeyTX = "TX";
        final String testStateKeyWA = "WA";
        State testTX = null;
        State testWA = null;
        List<State> allStatesFromFile = new ArrayList<>();

        //act
        try {
            testTX = testDao.readStateByID(testStateKeyTX);
            testWA = testDao.readStateByID(testStateKeyWA);

            allStatesFromFile = testDao.getValidStates();
        } catch (InvalidStateException | StateReadException e) {
            fail("Valid states");
        }

        //assert
        assertTrue(allStatesFromFile.contains(testTX), "List should contain Texas");
        assertTrue(allStatesFromFile.contains(testWA), "List should contain Washington");
    }

}
