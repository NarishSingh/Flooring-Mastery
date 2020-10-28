package com.sg.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditDaoImpl implements AuditDao {

    private static final String AUDIT_FILE = "audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws OrderPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not persist order audit", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();

        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }

}
