/*
* This file contains the StatementEntry class. An instance of this class
* corresponds to an entry in the Statement entries table of the database.
 */

package com.andrewhun.finance.models;

import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import java.util.Date;
import java.util.List;

import static com.andrewhun.finance.util.NamedConstants.*;

public abstract class StatementEntry implements ModifiableEntry {

    protected Integer id;
    protected String type;
    protected String title;
    protected Double amount;
    protected Integer userId;
    protected Date time;

    public static StatementEntry create
            (Integer id, String type, String title, Double amount, Integer userId, Date time) {

        if(type.equals(BALANCE)) {

            return new BalanceEntry(id, title, amount, userId, time);
        }
        else if (type.equals(INCOME)) {

            return new IncomeEntry(id, title, amount, userId, time);
        }
        else {
            return new ExpenseEntry(id, title, amount, userId, time);
        }
    }

    public static StatementEntry createIncompleteEntry(String type, String title, Double amount, Integer userId) {

        if (type.equals(BALANCE)) {
            return new BalanceEntry(title, amount, userId);
        }
        else if (type.equals(INCOME)) {
            return new IncomeEntry(title, amount, userId);
        }
        else {
            return new ExpenseEntry(title, amount, userId);
        }
    }

    public static void deleteAllEntriesForUser(Integer userId) throws Exception {

        List<StatementEntry> entries = new StatementEntryTableProcedures().findEntriesForUser(userId);
        for (StatementEntry entry : entries) {

            if (!entry.hasType(BALANCE)) {
                entry.delete();
            }
        }
    }

    public void createDatabaseRecord() throws Exception {

        new StatementEntryTableProcedures().addEntryToDatabase(this);
        User user = getUser();
        Double newBalance = calculateUserBalanceWhenEntryIsCreated(user.getBalance());
        user.updateBalance(newBalance);
    }

    public Boolean hasType(String type) {

        return this.getType().equals(type);
    }

    public void delete() throws Exception {

        new StatementEntryTableProcedures().deleteEntry(this);
        User user = getUser();
        Double newBalance = modifyUserBalanceWhenEntryIsDeleted(user.getBalance());
        user.updateBalance(newBalance);
    }

    public void edit() throws Exception {

        StatementEntryTableProcedures procedures = new StatementEntryTableProcedures();
        User user = getUser();
        StatementEntry originalEntry = procedures.findById(id);
        StatementEntry editedEntry;
        if (!this.type.equals(originalEntry.getType())) {
            editedEntry = StatementEntry.createIncompleteEntry(type, title, amount, userId);
        }
        else {
            editedEntry = this;
        }
        Double newBalance = adjustUserBalanceWhenEntryIsEdited(user.getBalance(), originalEntry, editedEntry);
        user.updateBalance(newBalance);

        procedures.editEntry(this);
    }

    protected abstract Double calculateUserBalanceWhenEntryIsCreated(Double balance);

    protected Double adjustUserBalanceWhenEntryIsEdited

            (Double balance, StatementEntry originalEntry, StatementEntry editedEntry) {

        balance = originalEntry.modifyUserBalanceWhenEntryIsDeleted(balance);
        balance = editedEntry.calculateUserBalanceWhenEntryIsCreated(balance);
        return balance;
    }

    protected abstract Double modifyUserBalanceWhenEntryIsDeleted(Double balance);

    private User getUser() throws Exception {

        return new UserTableProcedures().findById(userId);

    }

    public Integer getId() {

        return id;
    }

    public String getType() {

        return type;
    }

    public void setType(String newType) {

        type = newType;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String newTitle) {

        title = newTitle;
    }

    public Double getAmount() {

        return amount;
    }

    public void setAmount(Double newAmount) {

        amount = newAmount;
    }

    public Integer getUserId() {

        return userId;
    }

    public void setUserId(Integer newUserId) {

        userId = newUserId;
    }

    public Date getTime() {

        return time;
    }

    public void setTime(Date newTime) {

        time = newTime;
    }

    @Override
    public String toString() {

        return (type + " of " + amount + " with title " + title);
    }
}