package com.andrewhun.finance.services;

import java.util.List;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.RecurringEntry;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.NamedConstants.*;

class RecurringEntryServiceTest {

    private RecurringEntryService recurringEntryService = new RecurringEntryService();

    @BeforeEach
    void setUp() throws Exception {

        TestService.createRecurringEntriesTable();
        TestService.createDefaultRecurringEntry();
    }

    @Test
    void testFindById() throws Exception {

        RecurringEntry selectedEntry = recurringEntryService.findById(ID);
        Assertions.assertEquals(DAILY_WORK_INCOME, selectedEntry.toString());
    }

    @Test
    void testAddingRecurringEntry() throws Exception {

        addRentEntry();

        try {
            RecurringEntry newEntry = recurringEntryService.findById(SECOND_ID);
            Assertions.assertEquals(MONTHLY_RENT_EXPENSE, newEntry.toString());
        }
        catch (SQLException e) {

            fail("Did not create new recurring entry");
        }
    }

    @Test
    void testGetRecurringEntriesForUser() throws Exception {

        addRentEntry();
        recurringEntryService.addRecurringEntryToDatabase(EXPENSE, GROCERIES, TWENTY_FIVE, ID, WEEKLY);
        List<RecurringEntry> recurringEntries = recurringEntryService.getRecurringEntriesForUser(ID);
        Assertions.assertEquals(3, recurringEntries.size());
        Assertions.assertEquals(DAILY_WORK_INCOME, recurringEntries.get(0).toString());
        Assertions.assertEquals(MONTHLY_RENT_EXPENSE, recurringEntries.get(1).toString());
        Assertions.assertEquals(WEEKLY_GROCERY_EXPENSE, recurringEntries.get(2).toString());
    }

    private void addRentEntry() throws Exception {

        recurringEntryService.addRecurringEntryToDatabase(EXPENSE, RENT, FIVE_HUNDRED, ID, MONTHLY);
    }
}