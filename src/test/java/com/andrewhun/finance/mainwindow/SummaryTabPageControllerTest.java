package com.andrewhun.finance.mainwindow;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import javafx.geometry.VerticalDirection;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;

public class SummaryTabPageControllerTest extends TestFxBaseClass {

    @BeforeAll
     public static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        userService.loginUser(testUser);
        TestService.createRecurringEntriesTable();
    }

    @Test
    void testAllFieldsAreEmpty() {

        scrollDown();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testOnlyEntryTypeIsSelected() {

        scrollDown();
        selectIncomeAsEntryType();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryAmountAndFrequencyAreMissing() {

        scrollDown();
        selectIncomeAsEntryType();
        addWorkAsTitle();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryFrequencyIsMissing() {

        scrollDown();
        selectIncomeAsEntryType();
        addWorkAsTitle();
        add300AsAmount();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryTypeIsMissing() {

        scrollDown();
        addWorkAsTitle();
        add300AsAmount();
        selectDailyAsFrequency();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryTitleIsMissing() {

        scrollDown();
        selectIncomeAsEntryType();
        add300AsAmount();
        selectDailyAsFrequency();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testEntryAmountIsMissing() {

        scrollDown();
        selectIncomeAsEntryType();
        addWorkAsTitle();
        selectDailyAsFrequency();
        submitForm();
        testDisplayedErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test
    void testInvalidEntryAmount() {

        scrollDown();
        selectIncomeAsEntryType();
        addWorkAsTitle();
        clickOn(RECURRING_ENTRY_AMOUNT_FIELD_ID).write(INCORRECT_INPUT);
        selectDailyAsFrequency();
        submitForm();
        testDisplayedErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
    }

    @Test
    void testAddingRecurringEntry() throws Exception {

        RecurringEntryTableProcedures recurringEntryTableProcedures = new RecurringEntryTableProcedures();

        scrollDown();
        selectIncomeAsEntryType();
        addWorkAsTitle();
        add300AsAmount();
        selectDailyAsFrequency();
        submitForm();
        Assertions.assertEquals("Income of 300.0 with title Work recurring Daily.",
                recurringEntryTableProcedures.getEntryById(ID).toString());
    }

    private void testDisplayedErrorMessage(String message) {

        verifyThat(ADD_RECURRING_ENTRY_ERROR_ID, hasText(message));
    }

    private void selectIncomeAsEntryType() {

        clickOn(RECURRING_ENTRY_TYPE_BOX_ID);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void addWorkAsTitle() {

        clickOn(RECURRING_ENTRY_TITLE_FIELD_ID).write(WORK);
    }

    private void add300AsAmount() {

        clickOn(RECURRING_ENTRY_AMOUNT_FIELD_ID).write(THREE_HUNDRED.toString());
    }

    private void selectDailyAsFrequency() {

        clickOn(RECURRING_ENTRY_FREQUENCY_BOX_ID);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void submitForm() {

        clickOn(ADD_RECURRING_ENTRY_BUTTON_ID);
    }

    private void scrollDown() {

        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
        scroll(VerticalDirection.DOWN);
    }
}