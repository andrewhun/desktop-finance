/*
* This files contains the RecurringEntryService class, which handles
* the communication between the front-end processes and the database
* operations which are related to the Recurring entries table.
 */

package com.andrewhun.finance.services;

import java.util.List;
import com.andrewhun.finance.models.RecurringEntry;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;

public class RecurringEntryService {

    private RecurringEntryTableProcedures recurringEntryTableProcedures = new RecurringEntryTableProcedures();

    public RecurringEntry findById(Integer id) throws Exception {

        return recurringEntryTableProcedures.getEntryById(id);
    }

    public List<RecurringEntry> getRecurringEntriesForUser(Integer userId) throws Exception {

        return recurringEntryTableProcedures.getRecurringEntriesForUser(userId);
    }

    public void addRecurringEntryToDatabase(String type, String title, Double amount, Integer userId,
                                             String frequency) throws Exception {

        RecurringEntry recurringEntry = setUpRecurringEntry(type, title, amount, userId, frequency);
        recurringEntryTableProcedures.addRecurringEntryToDatabase(recurringEntry);
    }

    private RecurringEntry setUpRecurringEntry(String type, String title, Double amount, Integer userId,
                                               String frequency) {

        RecurringEntry recurringEntry = new RecurringEntry();
        recurringEntry.setType(type);
        recurringEntry.setTitle(title);
        recurringEntry.setAmount(amount);
        recurringEntry.setFrequency(frequency);
        recurringEntry.setUserId(userId);
        return recurringEntry;
    }
}