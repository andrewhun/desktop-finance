package com.andrewhun.finance.models;

import java.util.Date;
import java.util.List;

import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.exceptions.EntryNotFoundException;
import com.andrewhun.finance.services.TestService;
import org.junit.jupiter.api.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.fail;

class StatementEntryTest {

    private static StatementEntry statementEntry;
    private static Date originalTime = new Date();
    private static StatementEntryTableProcedures statementEntryTableProcedures =
            new StatementEntryTableProcedures();

    @BeforeEach
    void createStatementEntry() {

        statementEntry = StatementEntry.create(ID, INCOME, WORK, THREE_HUNDRED, ID, originalTime);

    }

    @Test void testGetId() {

        Assertions.assertEquals(ID, statementEntry.getId());
    }

    @Test void testGetType() {

        Assertions.assertEquals(INCOME, statementEntry.getType());
    }

    @Test void testSetType() {

        statementEntry.setType(EXPENSE);
        Assertions.assertEquals(EXPENSE, statementEntry.getType());
    }

    @Test void testGetTitle() {

        Assertions.assertEquals(WORK, statementEntry.getTitle());
    }

    @Test void testSetTitle() {

        statementEntry.setTitle(RENT);
        Assertions.assertEquals(RENT, statementEntry.getTitle());
    }

    @Test void testGetAmount() {

        Assertions.assertEquals(THREE_HUNDRED, statementEntry.getAmount());
    }

    @Test void testSetAmount() {

        statementEntry.setAmount(FIVE_HUNDRED);
        Assertions.assertEquals(FIVE_HUNDRED, statementEntry.getAmount());
    }

    @Test void testGetUserId() {

        Assertions.assertEquals(ID, statementEntry.getUserId());
    }

    @Test void testSetUserId() {

        statementEntry.setUserId(SECOND_ID);
        Assertions.assertEquals(SECOND_ID, statementEntry.getUserId());
    }

    @Test void testGetTime() {

        Assertions.assertEquals(originalTime, statementEntry.getTime());
    }

    @Test void testSetTime() {

        Date newTime = new Date();
        statementEntry.setTime(newTime);
        Assertions.assertEquals(newTime, statementEntry.getTime());
    }

    @Test void testCreateIncompleteEntry() {

        StatementEntry incompleteEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        Assertions.assertNull(incompleteEntry.getId());
        Assertions.assertNull(incompleteEntry.getTime());
        Assertions.assertEquals(RENT_STATEMENT_ENTRY, incompleteEntry.toString());
        Assertions.assertEquals(ID, incompleteEntry.getUserId());
    }

    @Test void testHasType() {

        StatementEntry expenseEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        Assertions.assertTrue(expenseEntry.hasType(EXPENSE));
        Assertions.assertFalse(expenseEntry.hasType(INCOME));

        StatementEntry incomeEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);
        Assertions.assertTrue(incomeEntry.hasType(INCOME));
        Assertions.assertFalse(incomeEntry.hasType(EXPENSE));
    }

    @Test void testDeleteIncomeEntry() throws Exception {

        testDeletingEntry(2);
    }

    @Test void testDeleteExpenseEntry() throws Exception {

        testDeletingEntry(3);
    }

    void testDeletingEntry(Integer entryId) throws Exception {

        setUpDatabase();

        UserTableProcedures userTableProcedures = new UserTableProcedures();

        Double originalBalance = userTableProcedures.findById(1).getBalance();

        StatementEntry selectedEntry = statementEntryTableProcedures.findById(entryId);
        selectedEntry.delete();

        Assertions.assertEquals(2, statementEntryTableProcedures.findEntriesForUser(1).size());

        if (selectedEntry.hasType(INCOME) || selectedEntry.hasType(BALANCE)) {
            Assertions.assertEquals((double) originalBalance - selectedEntry.getAmount(),
                    (double) userTableProcedures.findById(1).getBalance());
        }
        else if (selectedEntry.hasType(EXPENSE)) {
            Assertions.assertEquals((double) originalBalance + selectedEntry.getAmount(),
                    (double) userTableProcedures.findById(1).getBalance());
        }
    }

    private void setUpDatabase() throws Exception {

        User testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
        statementEntryTableProcedures.createTable();
        TestService.createStartingBalanceStatementEntry(testUser);
        TestService.createDefaultStatementEntry();
        StatementEntry expenseEntry = StatementEntry.create(SECOND_ID, EXPENSE, RENT,
                FIVE_HUNDRED, 1, new Date());
        expenseEntry.createDatabaseRecord();
    }

    @Test void testDeleteAllEntriesForUser() throws Exception {

        setUpDatabase();
        StatementEntry.deleteAllEntriesForUser(1);

        List<StatementEntry> entries = statementEntryTableProcedures.findEntriesForUser(1);
        Assertions.assertEquals(1, entries.size());
        Assertions.assertEquals(BALANCE, entries.get(0).getType());
        Assertions.assertEquals(FIVE_HUNDRED, entries.get(0).getAmount());

        User user = new UserTableProcedures().findById(1);
        Assertions.assertEquals(FIVE_HUNDRED, user.getBalance());
    }

    @Test void testChangeIncomeToExpense() throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry incomeEntry = statementEntryTableProcedures.findById(2);
        Assertions.assertEquals(INCOME, incomeEntry.getType());

        incomeEntry.setType(EXPENSE);
        incomeEntry.edit();

        Assertions.assertEquals(EXPENSE, statementEntryTableProcedures.findById(2).getType());
        Assertions.assertEquals(originalBalance - (incomeEntry.getAmount() * 2),
               User.getCurrentUserBalance(), 0.05);
    }

    @Test void testChangeExpenseToIncome() throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry expenseEntry = statementEntryTableProcedures.findById(3);

        expenseEntry.setType(INCOME);
        expenseEntry.edit();

        Assertions.assertEquals(INCOME, statementEntryTableProcedures.findById(3).getType());
        Assertions.assertEquals(originalBalance + (expenseEntry.getAmount() * 2),
               User.getCurrentUserBalance(), 0.05);
    }

    @Test void testChangeIncomeAmount() throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry incomeEntry = statementEntryTableProcedures.findById(2);
        Double originalAmount = incomeEntry.getAmount();
        incomeEntry.setAmount(FIVE_HUNDRED);
        incomeEntry.edit();

        Assertions.assertEquals(originalBalance -
                originalAmount + incomeEntry.getAmount(),
                User.getCurrentUserBalance(), 0.05);
    }

    @Test void testChangeExpenseAmount() throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry expenseEntry = statementEntryTableProcedures.findById(3);
        Double originalAmount = expenseEntry.getAmount();
        expenseEntry.setAmount(THREE_HUNDRED);
        expenseEntry.edit();

        Assertions.assertEquals(originalBalance + originalAmount - expenseEntry.getAmount(),
                User.getCurrentUserBalance(), 0.05);
    }

    @Test void testChangeBalanceEntryAmount() throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry balanceEntry = statementEntryTableProcedures.findById(1);
        Double originalAmount = balanceEntry.getAmount();
        balanceEntry.setAmount(5000.00);
        balanceEntry.edit();

        Assertions.assertEquals(originalBalance - originalAmount + balanceEntry.getAmount(),
               User.getCurrentUserBalance(), 0.05);
    }

    @Test void testCreateIncomeEntry() throws Exception {

        entryCreationTest(INCOME);
    }

    @Test void testCreateExpenseEntry() throws Exception {

        entryCreationTest(EXPENSE);
    }

    @Test void testCreateBalanceEntry() throws Exception {

        entryCreationTest(BALANCE);
    }

    private void entryCreationTest(String type) throws Exception {

        setUpDatabase();
        double originalBalance = User.getCurrentUserBalance();
        StatementEntry entry = createEntryInstance(type);
        entry.createDatabaseRecord();
        try {
            new StatementEntryTableProcedures().findById(4);
        } catch (EntryNotFoundException e) {

            fail("Entry was not added to the database");
        }
        assertNewUserBalance(originalBalance, entry);
    }

    private StatementEntry createEntryInstance(String type) throws Exception {

        Integer userId = User.getCurrentUserId();

        if (type.equals(BALANCE)) {

            return StatementEntry.createIncompleteEntry(BALANCE, STARTING_BALANCE,
                    FIVE_HUNDRED, userId);
        }
        else if(type.equals(INCOME)) {

            return StatementEntry.createIncompleteEntry(INCOME, WORK, TWENTY_FIVE, userId);
        }

        else {

            return StatementEntry.createIncompleteEntry(EXPENSE, GROCERIES, TWENTY_FIVE, userId);
        }
    }
    private void assertNewUserBalance(Double originalBalance, StatementEntry entry) throws Exception {

        double userBalance = User.getCurrentUserBalance();

        if(entry.hasType(BALANCE)) {

            Assertions.assertEquals(originalBalance, userBalance, 0.05);
        }
        else if (entry.hasType(INCOME)) {

            Assertions.assertEquals(originalBalance + entry.getAmount(),
                    userBalance, 0.05);
        }

        else {

            Assertions.assertEquals(originalBalance - entry.getAmount(),
                   userBalance, 0.05);
        }
    }
}