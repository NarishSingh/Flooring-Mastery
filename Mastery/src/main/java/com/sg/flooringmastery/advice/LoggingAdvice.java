package com.sg.flooringmastery.advice;

import com.sg.flooringmastery.dao.OrderPersistenceException;
import org.aspectj.lang.JoinPoint;
import com.sg.flooringmastery.dao.AuditDao;

public class LoggingAdvice {

    AuditDao auditDao;

    public LoggingAdvice(AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    /**
     * Create an audit entry that logs the method call and its arguments - used
     * in Spring AOP
     *
     * @param jp {JoinPoint} the method called at the pointcuts
     */
    public void createAuditEntry(JoinPoint jp) {
        Object[] args = jp.getArgs();
        String auditEntry = jp.getSignature().getName() + ": ";

        for (Object currentArgs : args) {
            auditEntry += currentArgs;
        }

        try {
            auditDao.writeAuditEntry(auditEntry);
        } catch (OrderPersistenceException e) {
            System.err.println("Error: could not create audit entry on method call");
        }
    }

    /**
     * Create and audit entry that logs the exceptions thrown and its joinpoint
     * at any point in the program - used in Spring AOP
     *
     * @param jp {JoinPoint} the method at which the exception it thrown
     * @param ex {Exception} the exception thrown by any method in the program
     */
    public void createExceptionWithJoinpointEntry(JoinPoint jp, Exception ex) {
        String auditEntry = "Thrown: " + jp.getSignature().getName() + ":: Message: " + ex.getMessage();

        try {
            auditDao.writeAuditEntry(auditEntry);
        } catch (OrderPersistenceException e) {
            System.err.println("Error: could not write audit entry on exception thrown");
        }
    }
}
