package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.AuditDao;
import com.sg.flooringmastery.dao.OrderPersistenceException;

public class AuditDaoImplStub implements AuditDao {

    @Override
    public void writeAuditEntry(String entry) throws OrderPersistenceException {
        //do nothing
    }

}
