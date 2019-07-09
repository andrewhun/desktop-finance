package com.andrewhun.finance.mainwindow;

import org.junit.jupiter.api.*;
import javafx.scene.input.KeyCode;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.RecurringEntry;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

class EditRecurringEntryFormControllerTest extends TestFxBaseClass {

    private RecurringEntry recurringEntry;
    private RecurringEntryTableProcedures recurringEntryTableProcedures
            = new RecurringEntryTableProcedures();

    @BeforeAll
    static void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
        TestService.createRecurringEntriesTable();
        TestService.createDefaultRecurringEntry();
    }

    @BeforeEach
    void navigateToForm() {

        clickOn(getNthRowInTableView(RECURRING_ENTRIES_TABLE_ID, 0));
        clickOn(EDIT_SELECTED_RECURRING_ENTRY_BUTTON_ID);
    }

    @AfterEach
    void renewTable() throws Exception {

        TestService.createRecurringEntriesTable();
        TestService.createDefaultRecurringEntry();
    }

    @Test
    void testClickingOnButtonClosesWindow() {

        submitForm();
        Assertions.assertTrue(stage.isFocused());
    }

    @Test
    void testChangingType() throws Exception {

        setTypeToExpense();
        submitForm();

        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(EXPENSE, recurringEntry.getType());
    }

    @Test
    void testChangingTitle() throws Exception {

        setTitleToRent();
        submitForm();

        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(RENT, recurringEntry.getTitle());
    }

    @Test
    void testChangingAmount() throws Exception {

        setAmountToFiveHundred();
        submitForm();

        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(FIVE_HUNDRED, recurringEntry.getAmount());
    }

    @Test
    void testBogusAmount() throws Exception {

        clickOn(EDIT_RECURRING_ENTRY_AMOUNT_ID).write(INCORRECT_INPUT);
        submitForm();

        verifyThat(EDIT_RECURRING_ENTRY_ERROR_ID, hasText(INVALID_NUMERIC_INPUT_MESSAGE));
        Assertions.assertFalse(stage.isFocused());
        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(THREE_HUNDRED, recurringEntry.getAmount());
    }

    @Test
    void testChangingFrequency() throws Exception {

        setFrequencyToMonthly();
        submitForm();

        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(MONTHLY, recurringEntry.getFrequency());
    }

    @Test
    void testChangingAll() throws Exception {

        setTypeToExpense();
        setTitleToRent();
        setAmountToFiveHundred();
        setFrequencyToMonthly();
        submitForm();

        recurringEntry = recurringEntryTableProcedures.findById(ID);
        Assertions.assertEquals(MONTHLY_RENT_EXPENSE, recurringEntry.toString());
    }

    private void setTypeToExpense() {

        clickOn(EDIT_RECURRING_ENTRY_TYPE_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void setTitleToRent() {

        clickOn(EDIT_RECURRING_ENTRY_TITLE_ID).write(RENT);
    }

    private void setAmountToFiveHundred() {

        clickOn(EDIT_RECURRING_ENTRY_AMOUNT_ID).write(FIVE_HUNDRED.toString());
    }

    private void setFrequencyToMonthly() {

        clickOn(EDIT_RECURRING_ENTRY_FREQUENCY_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void submitForm() {

        clickOn(SUBMIT_EDIT_RECURRING_ENTRY_FORM_BUTTON_ID);
    }
}