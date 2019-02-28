package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.RecurringEntry;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.services.RecurringEntryService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.EntryNotFoundException;

class RecurringEntryTableProceduresTest {

    private RecurringEntryService recurringEntryService = new RecurringEntryService();
    private RecurringEntryTableProcedures recurringEntryTableProcedures = new RecurringEntryTableProcedures();

    @BeforeAll
    static void setUp() {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
    }

    @BeforeEach
    void prepareDatabase() throws Exception {

        TestService.createRecurringEntriesTable();
        recurringEntryService.addRecurringEntryToDatabase(INCOME, WORK, THREE_HUNDRED, ID, DAILY);
    }

    @Test
    void testGettingEntryById() throws Exception {

        RecurringEntry selectedEntry =  recurringEntryTableProcedures.getEntryById(ID);
        Assertions.assertEquals(DAILY_WORK_INCOME, selectedEntry.toString());
    }

    @Test
    void testNoEntryFound() {

        assertThrows(EntryNotFoundException.class,
                () -> recurringEntryTableProcedures.getEntryById(SECOND_ID));
    }

    @Test
    void testGettingListOfEntriesForUser() throws Exception {

        addEntriesForUser();
        List<RecurringEntry> recurringEntries = recurringEntryTableProcedures.getRecurringEntriesForUser(ID);
        Assertions.assertEquals(3, recurringEntries.size());
    }

    private void addEntriesForUser() throws Exception {

        recurringEntryService.addRecurringEntryToDatabase(EXPENSE, GROCERIES, TWENTY_FIVE, ID, WEEKLY);
        recurringEntryService.addRecurringEntryToDatabase(EXPENSE, RENT, FIVE_HUNDRED, ID, MONTHLY);
    }
}