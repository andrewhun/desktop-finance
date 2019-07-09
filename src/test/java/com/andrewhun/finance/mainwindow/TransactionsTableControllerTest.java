package com.andrewhun.finance.mainwindow;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static com.andrewhun.finance.util.GuiElementIds.HISTORY_TAB_ID;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import com.andrewhun.finance.databaseprocedures.TransactionTableProcedures;
import static com.andrewhun.finance.util.GuiElementIds.DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID;

class TransactionsTableControllerTest extends TestFxBaseClass {


    private final String HIDE_TRANSACTIONS_TABLE_BUTTON_ID = "#hideTransactions";
    private final String SHOW_TRANSACTIONS_TABLE_BUTTON_ID = "#showTransactions";
    private final String TRANSACTIONS_LABEL_ID = "#transactionsLabel";
    private final String TRANSACTIONS_TABLE_ID = "#transactionsTable";

    @BeforeAll
    static void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();

        TransactionTableProcedures procedures = new TransactionTableProcedures();
        procedures.createTable();
        TestService.createBuyTransactionForUser(1);
        TestService.createSellTransactionForUser(1);
    }

    @BeforeEach
    void navigateToHistoryTab() throws Exception {

        setUp();

        clickOn(HISTORY_TAB_ID);
        moveTo(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
        scrollDown();
        scrollDown();
    }

    @Test
    void testHideTransactionsTable() {

        clickOn(HIDE_TRANSACTIONS_TABLE_BUTTON_ID);
        assertTrue(isVisibleElement(SHOW_TRANSACTIONS_TABLE_BUTTON_ID));
        assertFalse(isVisibleElement(HIDE_TRANSACTIONS_TABLE_BUTTON_ID));
        assertFalse(isVisibleElement(TRANSACTIONS_LABEL_ID));
        assertFalse(isVisibleElement(TRANSACTIONS_TABLE_ID));
    }

    @Test void testShowTransactionsTable() {

        clickOn(HIDE_TRANSACTIONS_TABLE_BUTTON_ID);
        clickOn(SHOW_TRANSACTIONS_TABLE_BUTTON_ID);

        assertFalse(isVisibleElement(SHOW_TRANSACTIONS_TABLE_BUTTON_ID));
        assertTrue(isVisibleElement(HIDE_TRANSACTIONS_TABLE_BUTTON_ID));
        assertTrue(isVisibleElement(TRANSACTIONS_LABEL_ID));
        assertTrue(isVisibleElement(TRANSACTIONS_TABLE_ID));
    }

    private Boolean isVisibleElement(String elementId) {

        return lookup(elementId).query().isVisible();
    }

    @Test void testTransactionsTableShowsEntries() {

        verifyThat(TRANSACTIONS_TABLE_ID, hasNumRows(2));
    }
}