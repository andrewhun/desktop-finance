package com.andrewhun.finance.models;

import java.util.Date;
import org.junit.jupiter.api.*;
import static com.andrewhun.finance.util.NamedConstants.*;

class StatementEntryTest {

    private static StatementEntry statementEntry;
    private static Date originalTime = new Date();

    @BeforeEach
    void createStatementEntry() {

        statementEntry = new StatementEntry(ID, INCOME, WORK, THREE_HUNDRED, ID, originalTime);
    }

    @Test void testGetId() {

        Assertions.assertEquals(ID, statementEntry.getId());
    }

    @Test void testSetId() {

        statementEntry.setId(SECOND_ID);
        Assertions.assertEquals(SECOND_ID, statementEntry.getId());
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
}