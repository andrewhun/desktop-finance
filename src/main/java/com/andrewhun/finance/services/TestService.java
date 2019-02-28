/*
* This file contains the TestService class, which handles basic database
* set up tasks for testing.
 */

package com.andrewhun.finance.services;

import com.andrewhun.finance.models.User;
import com.andrewhun.finance.databaseprocedures.*;
import com.andrewhun.finance.models.RecurringEntry;
import static com.andrewhun.finance.util.NamedConstants.*;

public class TestService {

    private static UserService userService = new UserService();
    private static RecurringEntryService recurringEntryService =
            new RecurringEntryService();
    private static UserTableProcedures userTableProcedures = new UserTableProcedures();
    private static RecurringEntryTableProcedures
            recurringEntryTableProcedures = new RecurringEntryTableProcedures();

    public static User setUpTestDatabaseWithTestUser() throws Exception {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        userTableProcedures.createUsersTable();
        addTestUserToDatabase(USERNAME);
        return userService.findByUsername(USERNAME);
    }

    public static void addTestUserToDatabase(String username) throws Exception {

        userTableProcedures.addUserToDatabase(createTestUser(username));
    }

    public static User createTestUser(String username) throws Exception {

        return userService.createIncompleteUserObject(username, PASSWORD, FIVE_HUNDRED);
    }

    public static void deleteTestUser(String username) throws Exception {

        userTableProcedures.deleteUser(username);
    }

    public static void createRecurringEntriesTable() throws Exception {

        recurringEntryTableProcedures.createRecurringEntriesTable();
    }

    public static RecurringEntry createDefaultRecurringEntry() throws Exception {

        recurringEntryService.addRecurringEntryToDatabase(INCOME, WORK, THREE_HUNDRED, ID, DAILY);
        return recurringEntryService.findById(ID);
    }
}
