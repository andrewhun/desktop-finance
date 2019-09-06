/*
 * This file contains the TestService class, which handles basic database
 * set up tasks for testing.
 */

package com.andrewhun.finance.services;

import java.sql.SQLException;
import java.util.List;

import com.andrewhun.finance.models.*;
import com.andrewhun.finance.databaseprocedures.*;

import static com.andrewhun.finance.util.NamedConstants.*;

public class TestService {

    private static UserTableProcedures userTableProcedures = new UserTableProcedures();
    private static RecurringEntryTableProcedures
            recurringEntryTableProcedures = new RecurringEntryTableProcedures();
    private static StatementEntryTableProcedures
            statementEntryTableProcedures = new StatementEntryTableProcedures();
    private static TransactionTableProcedures transactionTableProcedures =
            new TransactionTableProcedures();


    public static User setUpTestDatabaseWithTestUser() throws Exception {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        userTableProcedures.createTable();
        addTestUserToDatabase(USERNAME);
        return userTableProcedures.findByUsername(USERNAME);
    }

    public static void addTestUserToDatabase(String username) throws Exception {

        userTableProcedures.addEntryToDatabase(createTestUser(username));
    }

    public static User createTestUser(String username) throws Exception {

        return User.createIncompleteEntry(username, PASSWORD, FIVE_HUNDRED);
    }

    public static void deleteTestUser(String username) throws Exception {

        userTableProcedures.deleteUser(username);
    }

    public static void createRecurringEntriesTable() throws Exception {

        recurringEntryTableProcedures.createTable();
    }

    public static RecurringEntry createDefaultRecurringEntry() throws Exception {

        RecurringEntry recurringEntry =
                RecurringEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID, DAILY);

        recurringEntryTableProcedures.addEntryToDatabase(recurringEntry);
        return  recurringEntryTableProcedures.findById(ID);
    }

    public static StatementEntry createDefaultStatementEntry() throws Exception {

        StatementEntry statementEntry =
                StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);

        //statementEntryTableProcedures.addEntryToDatabase(statementEntry);
        statementEntry.createDatabaseRecord();
        List<StatementEntry> entries = statementEntryTableProcedures.findEntriesForUser(ID);
        for (StatementEntry entry: entries) {

            if (entry.hasType(INCOME)) {

                return entry;
            }
        }
        return null;
    }

    public static void createStartingBalanceStatementEntry(User user) throws Exception {

        StatementEntry balanceEntry =
                StatementEntry.createIncompleteEntry(BALANCE, STARTING_BALANCE,
                        user.getBalance(), user.getId());

        //statementEntryTableProcedures.addEntryToDatabase(balanceEntry);
        balanceEntry.createDatabaseRecord();

    }

    public static void createBuyTransactionForUser(Integer userId) throws SQLException {

        Transaction incompleteTransaction =
                Transaction.createIncompleteTransaction(userId, BUY,
                        NETFLIX, FIVE_HUNDRED, 10);
        transactionTableProcedures.addEntryToDatabase(incompleteTransaction);
    }

    public static void createSellTransactionForUser(Integer userId) throws SQLException {

        Transaction incompleteTransaction = Transaction.createIncompleteTransaction(userId, SELL,
                APPLE, THREE_HUNDRED, 5);
       transactionTableProcedures.addEntryToDatabase(incompleteTransaction);
    }
}