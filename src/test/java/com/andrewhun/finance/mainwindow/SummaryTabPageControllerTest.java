package com.andrewhun.finance.mainwindow;

import org.junit.jupiter.api.*;
import javafx.scene.input.KeyCode;
import org.testfx.api.FxRobotException;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;

public class SummaryTabPageControllerTest extends TestFxBaseClass {

    private RecurringEntryTableProcedures recurringEntryTableProcedures =
            new RecurringEntryTableProcedures();

    @BeforeAll
     public static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        testUser.login();
    }

    @BeforeEach
    void newDatabase() throws Exception {

        TestService.createRecurringEntriesTable();
        TestService.createDefaultRecurringEntry();
        moveTo(EDIT_SELECTED_RECURRING_ENTRY_BUTTON_ID);
        scrollDown();
    }


    @AfterEach
    void deleteAllEntries() throws Exception {

        recurringEntryTableProcedures.deleteAllEntriesForUser(ID);
    }

    @Test
    void testAllFieldsAreEmpty() {

        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testOnlyEntryTypeIsSelected() {

        selectExpenseAsEntryType();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryAmountAndFrequencyAreMissing() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryFrequencyIsMissing() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        add500AsAmount();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryTypeIsMissing() {

        addRentAsTitle();
        add500AsAmount();
        selectMonthlyAsFrequency();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryTitleIsMissing() {

        selectExpenseAsEntryType();
        add500AsAmount();
        selectMonthlyAsFrequency();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryAmountIsMissing() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        selectMonthlyAsFrequency();
        submitForm();
        checkDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testInvalidEntryAmount() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        clickOn(RECURRING_ENTRY_AMOUNT_FIELD_ID).write(INCORRECT_INPUT);
        selectMonthlyAsFrequency();
        submitForm();
        checkDisplayedErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
    }

    @Test
    void testAddingRecurringEntry() throws Exception {

        addRentEntry();
        Assertions.assertEquals(MONTHLY_RENT_EXPENSE,
                recurringEntryTableProcedures.findById(SECOND_ID).toString());
    }

    @Test
    void testRecurringEntryTableShowsNewEntries() {

        addRentEntry();
        verifyThat(RECURRING_ENTRIES_TABLE_ID, hasNumRows(2));
    }

    @Test
    void testDeletingRecurringEntries() throws Exception {

        addRentEntry();
        clickOn(getNthRowInTableView(RECURRING_ENTRIES_TABLE_ID, 0));
        clickOn(DELETE_SELECTED_RECURRING_ENTRY_BUTTON_ID);
        verifyThat(RECURRING_ENTRIES_TABLE_ID, hasNumRows(1));
        Assertions.assertEquals(1,
                recurringEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test void testPressingDeleteWithoutSelectingAnEntry() throws Exception {

        addRentEntry();
        clickOn(DELETE_SELECTED_RECURRING_ENTRY_BUTTON_ID);
        verifyThat(RECURRING_ENTRIES_TABLE_ID, hasNumRows(2));
        verifyRecurringEntriesTableError();
        Assertions.assertEquals(2,
                recurringEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test
    void testDeletingAllRecurringEntriesForUser() throws Exception {

        addRentEntry();
        clickOn(DELETE_ALL_RECURRING_ENTRIES_BUTTON_ID);
        verifyThat(RECURRING_ENTRIES_TABLE_ID, hasNumRows(0));
        Assertions.assertEquals(0,
                recurringEntryTableProcedures.findEntriesForUser(ID).size());
    }

    @Test
    void testEditFormPopsUpWhenEntryIsSelected() {

        addRentEntry();
        clickOn(getNthRowInTableView(RECURRING_ENTRIES_TABLE_ID, 0));
        clickOn(EDIT_SELECTED_RECURRING_ENTRY_BUTTON_ID);

        try {
            clickOn(SUBMIT_EDIT_RECURRING_ENTRY_FORM_BUTTON_ID);
        }
        catch (FxRobotException e) {

            fail("Edit entry form did not pop up.");
        }
    }

    @Test
    void testEditFormStaysHiddenWhenNoEntryIsSelected() {

        addRentEntry();
        clickOn(EDIT_SELECTED_RECURRING_ENTRY_BUTTON_ID);
        verifyRecurringEntriesTableError();
        assertThrows(FxRobotException.class,
                () -> clickOn(SUBMIT_EDIT_RECURRING_ENTRY_FORM_BUTTON_ID));
    }

    private void verifyRecurringEntriesTableError() {

        verifyThat(RECURRING_ENTRIES_TABLE_ERROR_ID, hasText(NO_ENTRY_SELECTED_MESSAGE));
    }

    private void checkDisplayedErrorMessage(String message) {

        verifyThat(ADD_RECURRING_ENTRY_ERROR_ID, hasText(message));
    }

    private void addRentEntry() {

        selectExpenseAsEntryType();
        addRentAsTitle();
        add500AsAmount();
        selectMonthlyAsFrequency();
        submitForm();
    }

    private void selectExpenseAsEntryType() {

        clickOn(RECURRING_ENTRY_TYPE_BOX_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void addRentAsTitle() {

        clickOn(RECURRING_ENTRY_TITLE_FIELD_ID).write(RENT);
    }

    private void add500AsAmount() {

        clickOn(RECURRING_ENTRY_AMOUNT_FIELD_ID).write(FIVE_HUNDRED.toString());
    }

    private void selectMonthlyAsFrequency() {

        clickOn(RECURRING_ENTRY_FREQUENCY_BOX_ID);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void submitForm() {

        clickOn(ADD_RECURRING_ENTRY_BUTTON_ID);
    }
}