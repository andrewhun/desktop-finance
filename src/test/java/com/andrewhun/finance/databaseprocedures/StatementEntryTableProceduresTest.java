package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.NamedConstants.*;

class StatementEntryTableProceduresTest {

    private static StatementEntryTableProcedures
            statementEntryTableProcedures = new StatementEntryTableProcedures();
    private static User testUser;

    @BeforeAll static void createTestUser() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @BeforeEach void setUpStatementEntriesTable() throws SQLException {

        statementEntryTableProcedures.createTable();
        StatementEntry incompleteEntry = StatementEntry.createIncompleteEntry(INCOME, WORK, THREE_HUNDRED, ID);
        statementEntryTableProcedures.addEntryToDatabase(incompleteEntry);
    }

    @Test void testAddEntryToDatabase() throws Exception {

        StatementEntry newEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        statementEntryTableProcedures.addEntryToDatabase(newEntry);
        StatementEntry secondEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(FIVE_HUNDRED, secondEntry.getAmount());
    }

    @Test void testFindById() throws Exception {

       StatementEntry statementEntry = statementEntryTableProcedures.findById(ID);
        Assertions.assertEquals(INCOME, statementEntry.getType());
    }

    @Test void testFindEntriesForUser() throws Exception {

        List<StatementEntry> statementEntries = statementEntryTableProcedures.findEntriesForUser(testUser.getId());
        Assertions.assertEquals(1, statementEntries.size());
    }

    @Test void testEditEntry() throws Exception {

        StatementEntry originalEntry = statementEntryTableProcedures.findById(ID);
        originalEntry.setType(EXPENSE);
        originalEntry.setTitle(RENT);
        originalEntry.setAmount(FIVE_HUNDRED);
        statementEntryTableProcedures.editEntry(originalEntry);

        StatementEntry editedEntry = statementEntryTableProcedures.findById(ID);
        Assertions.assertEquals(RENT_STATEMENT_ENTRY, editedEntry.toString());
    }

    @Test void testDeleteEntry() throws Exception {

        StatementEntry statementEntry = statementEntryTableProcedures.findById(ID);
        statementEntryTableProcedures.deleteEntry(statementEntry);

        Assertions.assertEquals(0,
                statementEntryTableProcedures.findEntriesForUser(testUser.getId()).size());
    }

    @Test void testDeleteAllEntriesForUser() throws Exception {

        StatementEntry secondEntry =
                StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, testUser.getId());

        statementEntryTableProcedures.addEntryToDatabase(secondEntry);

        statementEntryTableProcedures.deleteAllEntriesForUser(testUser.getId());

        Assertions.assertEquals(0,
                statementEntryTableProcedures.findEntriesForUser(testUser.getId()).size());
    }
}
