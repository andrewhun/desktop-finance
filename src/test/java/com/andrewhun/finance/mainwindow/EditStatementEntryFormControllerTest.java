package com.andrewhun.finance.mainwindow;

import com.andrewhun.finance.models.User;
import org.junit.jupiter.api.*;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

class EditStatementEntryFormControllerTest extends TestFxBaseClass {

    private  static StatementEntryTableProcedures statementEntryTableProcedures =
            new StatementEntryTableProcedures();

    @BeforeAll
    static void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();

        setUpStatementEntries();
    }

    @BeforeEach
    void navigateToForm() {

        clickOn(HISTORY_TAB_ID);
    }

    @AfterEach
    void renewTable() throws Exception {

        setUpStatementEntries();
    }

    private static void setUpStatementEntries() throws Exception {

        statementEntryTableProcedures.createTable();
        TestService.createStartingBalanceStatementEntry(testUser);
        TestService.createDefaultStatementEntry();

        StatementEntry rentEntry = StatementEntry.createIncompleteEntry(EXPENSE, RENT, FIVE_HUNDRED, ID);
        rentEntry.createDatabaseRecord();
    }

    @Test void testOnlyAmountIsChangeableForBalanceEntries() {

        selectBalanceEntry();

        ChoiceBox<String> editStatementEntryType = lookup(EDIT_STATEMENT_ENTRY_TYPE_ID).query();
        TextField editStatementEntryTitle = lookup(EDIT_STATEMENT_ENTRY_TITLE_ID).query();

        Assertions.assertTrue(editStatementEntryType.isDisable());
        Assertions.assertTrue(editStatementEntryTitle.isDisable());
    }

    @Test void testSubmittingFormClosesWindow() {

        selectIncomeEntry();
        submitForm();
        Assertions.assertTrue(stage.isFocused());
    }

    @Test void testChangeBalanceEntryAmount() throws Exception {

        double originalBalance = User.getCurrentUserBalance();
        selectBalanceEntry();
        clickOn(EDIT_STATEMENT_ENTRY_AMOUNT_ID).write(DEFAULT_BALANCE.toString());
        submitForm();

        double expectedBalance = originalBalance - FIVE_HUNDRED + DEFAULT_BALANCE;
        Assertions.assertEquals(expectedBalance, User.getCurrentUserBalance(), 0.005);
    }

    @Test void testChangeIncomeToExpense() throws Exception {

        selectIncomeEntry();
        double originalBalance = User.getCurrentUserBalance();
        setExpenseAsType();

        ChoiceBox choiceBox = lookup(EDIT_STATEMENT_ENTRY_TYPE_ID).query();
        Assertions.assertEquals(EXPENSE, choiceBox.getValue());

        submitForm();

        StatementEntry statementEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(EXPENSE, statementEntry.getType());

        /*
        * An income type statement entry is converted to an expense type one.
        * The income type's amount needs to be deducted from the user's balance, since there is one
        * less income entry in the database. The same amount then needs to be
        * deducted once more, as now there is an extra expense entry with that
        * amount.
        */

        double expectedBalance = originalBalance - (THREE_HUNDRED * 2);

        Assertions.assertEquals(expectedBalance, User.getCurrentUserBalance(), 0.005);
    }

    @Test void testChangingTitle() throws Exception {

        selectIncomeEntry();
        setRentAsTitle();
        submitForm();

        StatementEntry statementEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(RENT, statementEntry.getTitle());
    }

    @Test void testChangeAmountOnIncomeEntry() throws Exception {

        selectIncomeEntry();
        double originalAmount = User.getCurrentUserBalance();
        set500AsAmount();
        submitForm();

        StatementEntry statementEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(FIVE_HUNDRED, statementEntry.getAmount());

        /*
        * We change the amount of an income entry. The original amount needs to be deducted
        * from the user's balance and the edited amount needs to be added to it.
         */
        double expectedAmount = originalAmount - THREE_HUNDRED + FIVE_HUNDRED;
        Assertions.assertEquals(expectedAmount, User.getCurrentUserBalance(), 0.005);
    }

    @Test void testBogusAmount() throws Exception {

        selectIncomeEntry();
        clickOn(EDIT_STATEMENT_ENTRY_AMOUNT_ID).write(INCORRECT_INPUT);
        submitForm();

        verifyThat(EDIT_STATEMENT_ENTRY_ERROR_ID, hasText(INVALID_NUMERIC_INPUT_MESSAGE));

        StatementEntry statementEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(THREE_HUNDRED, statementEntry.getAmount());
    }

    @Test void testChangingAll() throws Exception {

        selectIncomeEntry();
        setExpenseAsType();
        setRentAsTitle();
        set500AsAmount();
        submitForm();

        StatementEntry statementEntry = statementEntryTableProcedures.findById(SECOND_ID);
        Assertions.assertEquals(RENT_STATEMENT_ENTRY, statementEntry.toString());
    }

    @Test void testChangeExpenseToIncome() throws Exception {

        double originalBalance = User.getCurrentUserBalance();
        selectExpenseEntry();
        clickOn(EDIT_STATEMENT_ENTRY_TYPE_ID);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        submitForm();

        /*
        * We turn an expense entry into an income entry. The amount of the expense entry
        * needs to be added to the user's balance, as there is now one less expense entry in
        * the database. The same amount needs to be added once more,
        * since there is an extra income entry with the same amount.
         */
        double expectedBalance = originalBalance + (FIVE_HUNDRED * 2);
        Assertions.assertEquals(expectedBalance, User.getCurrentUserBalance(), 0.005);
    }

    @Test void testChangeAmountOnExpenseEntry() throws Exception {

        double originalBalance = User.getCurrentUserBalance();
        selectExpenseEntry();
        clickOn(EDIT_STATEMENT_ENTRY_AMOUNT_ID).write(THREE_HUNDRED.toString());
        submitForm();

        /*
        * We change the amount of an expense entry. The original amount needs to be added
        * to the user's balance and the edited amount needs to be deducted from it.
         */
        double expectedBalance = originalBalance + FIVE_HUNDRED - THREE_HUNDRED;
        Assertions.assertEquals(expectedBalance, User.getCurrentUserBalance(), 0.005);
    }

    private void setExpenseAsType() {

        clickOn(EDIT_STATEMENT_ENTRY_TYPE_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void setRentAsTitle() {

        clickOn(EDIT_STATEMENT_ENTRY_TITLE_ID).write(RENT);
    }

    private void set500AsAmount() {

        clickOn(EDIT_STATEMENT_ENTRY_AMOUNT_ID).write(FIVE_HUNDRED.toString());
    }

    private void submitForm() {

        clickOn(SUBMIT_EDIT_STATEMENT_ENTRY_FORM_BUTTON_ID);
    }

    private void selectBalanceEntry() {

        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 0));
        openEditForm();
    }

    private void selectIncomeEntry() {

        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 1));
        openEditForm();
    }

    private void selectExpenseEntry() {

        clickOn(getNthRowInTableView(STATEMENT_ENTRIES_TABLE_ID, 2));
        openEditForm();
    }

    private void openEditForm() {

        clickOn(EDIT_SELECTED_STATEMENT_ENTRY_BUTTON_ID);
    }
}
