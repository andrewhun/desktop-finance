package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.RecurringEntry;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.EntryNotFoundException;

class RecurringEntryTableProceduresTest {

    private RecurringEntryTableProcedures recurringEntryTableProcedures = new RecurringEntryTableProcedures();
    private RecurringEntry recurringEntry;

    @BeforeAll
    static void setUp() {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
    }

    @BeforeEach
    void prepareDatabase() throws Exception {

        TestService.createRecurringEntriesTable();
        recurringEntry = TestService.createDefaultRecurringEntry();
    }

    @Test
    void testGettingEntryById() throws Exception {

        RecurringEntry selectedEntry =  recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(DAILY_WORK_INCOME, selectedEntry.toString());
    }

    @Test
    void testNoEntryFound() {

        assertThrows(EntryNotFoundException.class,
                () -> recurringEntryTableProcedures.findById(SECOND_ID));
    }

    @Test
    void testGettingListOfEntriesForUser() throws Exception {

        addEntriesForUser();
        List<RecurringEntry> recurringEntries = recurringEntryTableProcedures.findEntriesForUser(ID);
        Assertions.assertEquals(3, recurringEntries.size());
    }

    private void addEntriesForUser() throws Exception {

        RecurringEntry groceryEntry =
                RecurringEntry.createIncompleteEntry(EXPENSE, GROCERIES, TWENTY_FIVE, ID, WEEKLY);
        recurringEntryTableProcedures.addEntryToDatabase(groceryEntry);

        RecurringEntry rentEntry =
                RecurringEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID, MONTHLY);
        recurringEntryTableProcedures.addEntryToDatabase(rentEntry);
    }

    @Test
    void testFindingEmptyListOfEntries() throws Exception {

        List<RecurringEntry> recurringEntries =
                recurringEntryTableProcedures.findEntriesForUser(SECOND_ID);

        Assertions.assertEquals(0, recurringEntries.size());
    }

    @Test
    void testDeletingRecurringEntry() throws Exception {

        recurringEntryTableProcedures.deleteEntry(recurringEntry);
        try {

            recurringEntryTableProcedures.findById(ID);
            fail("Did not delete entry");
        }
        catch (EntryNotFoundException ignore) {}
    }

    @Test
    void testDeletingAllEntriesForUser() throws Exception {

        addEntriesForUser();
        recurringEntryTableProcedures.deleteAllEntriesForUser(ID);
        Assertions.assertEquals(0, recurringEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test
    void testEditingEntries() throws Exception {

        RecurringEntry recurringEntry = recurringEntryTableProcedures.findById(ID);
        recurringEntry.setType(EXPENSE);
        recurringEntry.setTitle(GROCERIES);
        recurringEntry.setAmount(TWENTY_FIVE);
        recurringEntry.setFrequency(WEEKLY);
        recurringEntryTableProcedures.editEntry(recurringEntry);
        Assertions.assertEquals(WEEKLY_GROCERY_EXPENSE,
                recurringEntryTableProcedures.findById(ID).toString());
    }
}