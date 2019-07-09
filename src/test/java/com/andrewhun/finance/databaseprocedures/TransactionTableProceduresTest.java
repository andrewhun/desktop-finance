package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import java.sql.SQLException;

import com.andrewhun.finance.services.TestService;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.Transaction;
import com.andrewhun.finance.util.NamedConstants;


import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTableProceduresTest {

    private TransactionTableProcedures tableProcedures = new TransactionTableProcedures();

    @BeforeEach void setUp() throws SQLException {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        tableProcedures.createTable();
        TestService.createBuyTransactionForUser(1);
    }

    @Test void testAddEntryToDatabase() throws Exception {

        TestService.createSellTransactionForUser(1);
        Transaction sellTransaction = tableProcedures.findById(NamedConstants.SECOND_ID);
        assertEquals(NamedConstants.SELL, sellTransaction.getType());
    }

    @Test void testFindById() throws Exception {

        Transaction transaction = tableProcedures.findById(NamedConstants.ID);
        assertEquals(NamedConstants.BUY, transaction.getType());
    }

    @Test void testFindTransactionsForUser() throws Exception {

        TestService.createSellTransactionForUser(1);
        assertNumberOfTransactionsForUser(1, 2);
    }

    @Test void testDeleteAllTransactionsForUser() throws Exception {

        TestService.createSellTransactionForUser(1);

        TestService.createBuyTransactionForUser(2);
        TestService.createSellTransactionForUser(2);

        tableProcedures.deleteAllTransactionsForUser(1);
        assertNumberOfTransactionsForUser(1, 0);
        assertNumberOfTransactionsForUser(2, 2);
    }

    private void assertNumberOfTransactionsForUser
            (Integer userId, int numberOfTransactions) throws Exception {

        List<Transaction> transactions = tableProcedures.findEntriesForUser(userId);
        assertEquals(numberOfTransactions, transactions.size());
    }
}