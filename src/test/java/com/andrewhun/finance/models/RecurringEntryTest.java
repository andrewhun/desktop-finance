package com.andrewhun.finance.models;

import java.util.Date;
import org.junit.jupiter.api.*;
import static com.andrewhun.finance.util.NamedConstants.*;

class RecurringEntryTest {

    private Date originalTime = new Date();
    private RecurringEntry recurringEntry =
            new RecurringEntry(ID, INCOME, WORK, THREE_HUNDRED, ID, originalTime, DAILY);

    @Test
    void testGetId() {

        Assertions.assertEquals(ID, recurringEntry.getId());
    }

    @Test
    void setId() {

        recurringEntry.setId(SECOND_ID);
        Assertions.assertEquals(SECOND_ID, recurringEntry.getId());
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
}