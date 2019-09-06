package com.andrewhun.finance.models;

import java.util.Date;

import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;
import com.andrewhun.finance.services.TestService;
import org.junit.jupiter.api.*;
import static com.andrewhun.finance.util.NamedConstants.*;

class RecurringEntryTest {

    private Date originalTime = new Date();
    private RecurringEntry recurringEntry =
            new RecurringEntry(ID, INCOME, WORK, THREE_HUNDRED, ID, originalTime, DAILY);

    private RecurringEntryTableProcedures procedures = new RecurringEntryTableProcedures();

    @Test
    void testGetId() {

        Assertions.assertEquals(ID, recurringEntry.getId());
    }

    @Test
    void testGetType() {

        Assertions.assertEquals(INCOME, recurringEntry.getType());
    }

    @Test
    void testSetType() {

        recurringEntry.setType(EXPENSE);
        Assertions.assertEquals(EXPENSE, recurringEntry.getType());
    }

    @Test
    void testGetTitle() {

        Assertions.assertEquals(WORK, recurringEntry.getTitle());
    }

    @Test
    void testSetTitle() {

        recurringEntry.setTitle(RENT);
        Assertions.assertEquals(RENT, recurringEntry.getTitle());
    }

    @Test
    void testGetAmount() {

        Assertions.assertEquals(THREE_HUNDRED, recurringEntry.getAmount());
    }

    @Test
    void testSetAmount() {

        recurringEntry.setAmount(FIVE_HUNDRED);
        Assertions.assertEquals(FIVE_HUNDRED, recurringEntry.getAmount());
    }

    @Test
    void testGetUserId() {

        Assertions.assertEquals(ID, recurringEntry.getUserId());
    }

    @Test
    void testSetUserId() {

        recurringEntry.setUserId(SECOND_ID);
        Assertions.assertEquals(SECOND_ID, recurringEntry.getUserId());
    }

    @Test
    void testGetTime() {

        Assertions.assertEquals(originalTime, recurringEntry.getTime());
    }

    @Test
    void testSetTime() {

        Date newTime = new Date();
        recurringEntry.setTime(newTime);
        Assertions.assertEquals(newTime, recurringEntry.getTime());
    }

    @Test
    void testGetFrequency() {

        Assertions.assertEquals(DAILY, recurringEntry.getFrequency());
    }

    @Test
    void testSetFrequency() {

        recurringEntry.setFrequency(MONTHLY);
        Assertions.assertEquals(MONTHLY, recurringEntry.getFrequency());
    }

    @Test
    void testCreateIncompleteEntry() {

        RecurringEntry incompleteEntry =
                RecurringEntry.createIncompleteEntry(EXPENSE, GROCERIES, TWENTY_FIVE, ID, WEEKLY);

        Assertions.assertEquals(WEEKLY_GROCERY_EXPENSE, incompleteEntry.toString());
        Assertions.assertNull(incompleteEntry.getId());
        Assertions.assertNull(incompleteEntry.getTime());

    }

    @Test void testDelete() throws Exception {

        setUpDatabase();
        RecurringEntry recurringEntry = procedures.findById(ID);
        recurringEntry.delete();

        Assertions.assertEquals(2,
                procedures.findEntriesForUser(User.getCurrentUserId()).size());
    }

    @Test void testDeleteAllEntriesForUser() throws Exception {

        setUpDatabase();
        RecurringEntry.deleteAllEntriesForUser(ID);
        Assertions.assertEquals(0, procedures.findEntriesForUser(ID).size());
    }

    @Test void testEditType() throws Exception {

        setUpDatabase();
        RecurringEntry groceryEntry = procedures.findById(2);
        Assertions.assertEquals(EXPENSE, groceryEntry.getType());

        groceryEntry.setType(INCOME);
        groceryEntry.edit();
        Assertions.assertEquals(INCOME, procedures.findById(2).getType());

    }

    @Test void testEditTitle() throws Exception {

        setUpDatabase();
        RecurringEntry groceryEntry = procedures.findById(2);
        Assertions.assertEquals(GROCERIES, groceryEntry.getTitle());

        groceryEntry.setTitle(RENT);
        groceryEntry.edit();
        Assertions.assertEquals(RENT, procedures.findById(2).getTitle());
    }

    @Test void testEditAmount() throws Exception {

        setUpDatabase();
        RecurringEntry groceryEntry = procedures.findById(2);
        Assertions.assertEquals(TWENTY_FIVE, groceryEntry.getAmount());

        groceryEntry.setAmount(FIVE_HUNDRED);
        groceryEntry.edit();
        Assertions.assertEquals(FIVE_HUNDRED, procedures.findById(2).getAmount());
    }

    @Test void testEditFrequency() throws Exception {

        setUpDatabase();
        RecurringEntry groceryEntry = procedures.findById(2);
        Assertions.assertEquals(WEEKLY, groceryEntry.getFrequency());

        groceryEntry.setFrequency(DAILY);
        groceryEntry.edit();

        Assertions.assertEquals(DAILY, procedures.findById(2).getFrequency());
    }

    @Test void testChangingAll() throws Exception {

        setUpDatabase();
        RecurringEntry incomeEntry = procedures.findById(1);
        Assertions.assertEquals(INCOME, incomeEntry.getType());
        Assertions.assertEquals(WORK, incomeEntry.getTitle());
        Assertions.assertEquals(THREE_HUNDRED, incomeEntry.getAmount());
        Assertions.assertEquals(DAILY, incomeEntry.getFrequency());

        incomeEntry.setType(EXPENSE);
        incomeEntry.setTitle(RENT);
        incomeEntry.setAmount(FIVE_HUNDRED);
        incomeEntry.setFrequency(MONTHLY);
        incomeEntry.edit();

        RecurringEntry editedEntry = procedures.findById(1);
        Assertions.assertEquals(EXPENSE, editedEntry.getType());
        Assertions.assertEquals(RENT, editedEntry.getTitle());
        Assertions.assertEquals(FIVE_HUNDRED, editedEntry.getAmount());
        Assertions.assertEquals(MONTHLY, editedEntry.getFrequency());
    }

    private void setUpDatabase() throws Exception {

        User testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
        TestService.createRecurringEntriesTable();
        TestService.createDefaultRecurringEntry();

        RecurringEntry groceryEntry =
                RecurringEntry.createIncompleteEntry(EXPENSE, GROCERIES, TWENTY_FIVE, testUser.getId(), WEEKLY);
        procedures.addEntryToDatabase(groceryEntry);

        RecurringEntry rentEntry =
                RecurringEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, testUser.getId(), MONTHLY);
        procedures.addEntryToDatabase(rentEntry);

    }

}