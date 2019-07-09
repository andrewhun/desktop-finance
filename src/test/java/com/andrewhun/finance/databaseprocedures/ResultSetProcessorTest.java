package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import java.sql.ResultSet;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.exceptions.*;
import com.andrewhun.finance.models.ModifiableEntry;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultSetProcessorTest {

    private ResultSetProcessor<ModifiableEntry> resultSetProcessor;
    private TestResultSet testResultSet;

    @BeforeEach void setUp() {

        resultSetProcessor = new ResultSetProcessor<>() {
            @Override
            ModifiableEntry createModelFromResultSet(ResultSet resultSet) {
                return new ModifiableEntry() {};
            }
        };
        testResultSet = new TestResultSet();
    }

    @Test void testGetSingleEntry() throws Exception {

        testResultSet.setNumberOfEntries(1);
        ModifiableEntry entry = resultSetProcessor.getSingleEntry(testResultSet);
        Assertions.assertNotNull(entry);
    }

    @Test void testEntryNotFoundException() {

        assertThrows(EntryNotFoundException.class, ()->  resultSetProcessor.getSingleEntry(testResultSet));
    }

    @Test void testMultipleEntriesFoundException() {

        testResultSet.setNumberOfEntries(2);
        assertThrows(MultipleEntriesFoundException.class, () -> resultSetProcessor.getSingleEntry(testResultSet));
    }

    @Test void testGetListOfEntries() throws Exception {

        testResultSet.setNumberOfEntries(3);
        List<ModifiableEntry> entries = resultSetProcessor.getListOfEntries(testResultSet);
        Assertions.assertEquals(3, entries.size());
    }
}