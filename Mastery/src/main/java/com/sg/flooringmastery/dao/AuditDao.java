package com.sg.flooringmastery.dao;

public interface AuditDao {

    /**
     * Create a timestamped entry logging the argumentized String to file
     *
     * @param entry {String} what should be logged
     * @throws OrderPersistenceException if cannot write to audit file
     */
    public void writeAuditEntry(String entry) throws OrderPersistenceException;
}
