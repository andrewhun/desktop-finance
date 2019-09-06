package com.andrewhun.finance.mainwindow;

import java.util.List;

import com.andrewhun.finance.models.User;
import javafx.scene.Node;
import org.junit.jupiter.api.*;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ChoiceBox;
import org.testfx.api.FxRobotException;
import com.andrewhun.finance.TestFxBaseClass;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;

import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;

class HistoryTabPageControllerTest extends TestFxBaseClass {

    private static StatementEntryTableProcedures
            statementEntryTableProcedures = new StatementEntryTableProcedures();

    @BeforeAll static void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
        statementEntryTableProcedures.createTable();
        TestService.createStartingBalanceStatementEntry(testUser);
        TestService.createDefaultStatementEntry();
    }

    @BeforeEach
    void navigateToHistoryTab() throws Exception {

        setUp();

        clickOn(HISTORY_TAB_ID);
        moveTo(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
        scrollDown();
    }

    @Test void testAllFieldsAreLeftEmpty() {

        submitForm();

        verifyThat(ADD_STATEMENT_ENTRY_ERROR_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test void testOnlyEntryTypeIsFilled() {

        scrollUp();
        selectExpenseAsEntryType();
        ChoiceBox statementEntryType = lookup(STATEMENT_ENTRY_TYPE_ID).query();
        Assertions.assertEquals(EXPENSE,statementEntryType.getValue());
        submitForm();

        verifyThat(ADD_STATEMENT_ENTRY_ERROR_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test void testAmountIsMissing() {

        scrollUp();
        selectExpenseAsEntryType();
        addRentAsTitle();
        submitForm();
        verifyThat(ADD_STATEMENT_ENTRY_ERROR_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test void testTypeIsMissing() {

        scrollUp();
        addRentAsTitle();
        add500AsAmount();
        submitForm();
        verifyThat(ADD_STATEMENT_ENTRY_ERROR_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test void testInvalidAmount() {

        scrollUp();
        selectExpenseAsEntryType();
        addRentAsTitle();
        clickOn(STATEMENT_ENTRY_AMOUNT_ID).write(INCORRECT_INPUT);
        submitForm();
        verifyThat(ADD_STATEMENT_ENTRY_ERROR_ID, hasText(INVALID_NUMERIC_INPUT_MESSAGE));
    }

    @Test void testAddExpenseEntry() throws Exception {

        scrollUp();
        double originalBalance = User.getCurrentUserBalance();
        addRentEntry();
        Assertions.assertEquals(3, statementEntryTableProcedures.findEntriesForUser(testUser.getId()).size());
        verifyThat(STATEMENT_ENTRIES_TABLE_ID, hasNumRows(3));
        StatementEntry rentEntry = statementEntryTableProcedures.findById(3);

        // The new expense entry's amount needs to be deducted from the user's balance
        Assertions.assertEquals(originalBalance - rentEntry.getAmount(),
                User.getCurrentUserBalance(), 0.005);
    }

    @Test void testAddIncomeEntry() throws Exception {

        double originalBalance = User.getCurrentUserBalance();

        scrollUp();
        clickOn(STATEMENT_ENTRY_TYPE_ID);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        clickOn(STATEMENT_ENTRY_TITLE_ID).write(WORK);

        clickOn(STATEMENT_ENTRY_AMOUNT_ID).write(THREE_HUNDRED.toString());

        clickOn(ADD_STATEMENT_ENTRY_BUTTON_ID);

        // The new income entry's amount needs to be added to the user's balance
        Assertions.assertEquals(originalBalance + THREE_HUNDRED,
                User.getCurrentUserBalance(), 0.005);
    }

    @Test void testDeleteIncomeEntry() throws Exception {

        scrollUp();
        addRentEntry();
        double originalBalance = User.getCurrentUserBalance();
        scrollUp();
        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 1));
        clickOn(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
        verifyThat(STATEMENT_ENTRIES_TABLE_ID, hasNumRows(2));
        Assertions.assertEquals(2, statementEntryTableProcedures.findEntriesForUser(ID).size());

        // The deleted income entry's amount needs to be deducted from the user's balance
        Assertions.assertEquals(originalBalance - THREE_HUNDRED,
                User.getCurrentUserBalance(), 0.005);
    }

    @Test void testDeleteExpenseEntry() throws Exception {

        scrollUp();
        addRentEntry();
        double originalBalance = User.getCurrentUserBalance();
        scrollUp();
        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 2));
        clickOn(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);

        //The deleted expense entry's amount needs to be added to the user's balance
        Assertions.assertEquals(originalBalance + FIVE_HUNDRED,
                User.getCurrentUserBalance(), 0.005);
    }

    @Test void testTryingToDeleteBalanceEntry() throws Exception {

        scrollUp();
        addRentEntry();
        scrollUp();
        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 0));
        clickOn(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);

        Assertions.assertEquals(3, statementEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test void testPressingDeleteButtonWithoutSelectingEntry() throws Exception {

        scrollUp();
        addRentEntry();
        scrollUp();
        clickOn(DELETE_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
        verifyThat(STATEMENT_ENTRIES_TABLE_ID, hasNumRows(3));
        verifyStatementEntriesTableError();
        Assertions.assertEquals(3, statementEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test void testAccountResetConfirmationShowsUp() {

        scrollUp();
        scrollUp();
        clickOn(DELETE_ALL_STATEMENT_ENTRIES_BUTTON_ID);
        Assertions.assertFalse(stage.isFocused());

        try {
            clickOn((Node) lookup("Cancel").query());
        }
        catch (FxRobotException e) {

            fail("Could not find Cancel button because the confirmation dialog did not show up.");
        }
    }

    @Test void testDeleteAllEntries() throws Exception {

        scrollUp();
        addRentEntry();
        scrollUp();
        clickOn(DELETE_ALL_STATEMENT_ENTRIES_BUTTON_ID);
        clickOn((Node)lookup("OK").query());

        List<StatementEntry> statementEntries = statementEntryTableProcedures.findEntriesForUser(ID);
        Assertions.assertEquals(1, statementEntries.size());
        StatementEntry balanceEntry = statementEntries.get(0);
        Assertions.assertEquals(FIVE_HUNDRED, balanceEntry.getAmount());
        verifyThat(STATEMENT_ENTRIES_TABLE_ID, hasNumRows(1));

        // The user's balance needs to be reset to the original amount
        Assertions.assertEquals(FIVE_HUNDRED, User.getCurrentUserBalance(), 0.005);
    }

    @Test void testCancelAccountReset() throws Exception {

        scrollUp();
        addRentEntry();
        scrollUp();
        clickOn(DELETE_ALL_STATEMENT_ENTRIES_BUTTON_ID);
        clickOn((Node)lookup("Cancel").query());

        Assertions.assertEquals(3, statementEntryTableProcedures.findEntriesForUser(ID).size());
        verifyThat(STATEMENT_ENTRIES_TABLE_ID, hasNumRows(3));
    }

    @Test void testEditFormShowsUpWhenEntryIsSelected() {

        scrollUp();
        addRentEntry();
        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 0));
        clickOn(EDIT_SELECTED_STATEMENT_ENTRY_BUTTON_ID);

        try {
            clickOn(SUBMIT_EDIT_STATEMENT_ENTRY_FORM_BUTTON_ID);
        }
        catch (FxRobotException e) {

            fail("Edit form did not show up");
        }
    }

    @Test void testEditFormStaysHiddenWhenNoEntryIsSelected() {

        scrollUp();
        scrollUp();
        clickOn(EDIT_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
        verifyStatementEntriesTableError();
        assertThrows(FxRobotException.class,
                () -> clickOn(SUBMIT_EDIT_RECURRING_ENTRY_FORM_BUTTON_ID));
    }

    private void verifyStatementEntriesTableError() {

        verifyThat(STATEMENT_ENTRIES_TABLE_ERROR_ID, hasText(NO_ENTRY_SELECTED_MESSAGE));
    }

    private void addRentEntry() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        add500AsAmount();
        submitForm();
    }

    private void selectExpenseAsEntryType() {

        clickOn(STATEMENT_ENTRY_TYPE_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void addRentAsTitle() {

        clickOn(STATEMENT_ENTRY_TITLE_ID).write(RENT);
    }

    private void add500AsAmount() {

        clickOn(STATEMENT_ENTRY_AMOUNT_ID).write(FIVE_HUNDRED.toString());
    }

    private void submitForm() {

        clickOn(ADD_STATEMENT_ENTRY_BUTTON_ID);
    }
}