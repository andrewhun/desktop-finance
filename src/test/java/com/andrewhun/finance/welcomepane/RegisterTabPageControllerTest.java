package com.andrewhun.finance.welcomepane;

import java.util.List;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import com.andrewhun.finance.services.TestService;
import com.andrewhun.finance.models.StatementEntry;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.andrewhun.finance.exceptions.EntryNotFoundException;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;

class RegisterTabPageControllerTest extends TestFxBaseClass {

    private static StatementEntryTableProcedures
            statementEntryTableProcedures = new StatementEntryTableProcedures();

    @BeforeAll static void setUp() throws Exception {

        TestFxBaseClass.setUpTestDatabase();
        statementEntryTableProcedures.createTable();
    }

    @AfterEach
    void logoutUser() throws Exception {

        testUser.logout();
        super.tearDown();
    }

    @BeforeEach
    void moveToRegisterTab() {

        clickOn(REGISTER_TAB_ID);
    }

    @Test
    void testAllFieldsLeftEmpty() {

        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testOnlyUsernameIsFilled() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testConfirmationIsLeftEmpty() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testUsernameIsLeftEmpty() {

        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testRequiredFieldsAreFilled() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(INCORRECT_INPUT);
        clickOn(REGISTER_BUTTON_ID);
        // Should not show the "required field empty" error message
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(PASSWORD_MISMATCH_MESSAGE));
    }

    @Test
    void testPasswordMismatch() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(SECOND_USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(INCORRECT_INPUT);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(PASSWORD_MISMATCH_MESSAGE));
    }

    @Test
    void testUsernameIsTaken() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID,
                hasText(USERNAME_TAKEN_MESSAGE));
    }

    @Test
    void testInvalidBalanceInput() {

        enterCorrectCredentials();
        clickOn(REGISTER_STARTING_BALANCE_FIELD_ID).write(INCORRECT_INPUT);
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID,
                hasText(INVALID_NUMERIC_INPUT_MESSAGE));
    }

    @Test
    void testCreateUserWithDefaultBalance() throws Exception {

        enterCorrectCredentials();
        clickOn(REGISTER_BUTTON_ID);

        try {
            Assertions.assertTrue(userHasBalance(DEFAULT_BALANCE));
        }
        catch (EntryNotFoundException e) {

            fail("Did not create user with default balance");
        }

        testStatementEntryIsCreatedWithAmount(DEFAULT_BALANCE);
        // Remove user to allow the reuse of the same username
        TestService.deleteTestUser(SECOND_USERNAME);
    }

    @Test
    void testCreateUserWithCustomBalance() throws Exception {

        enterCorrectCredentials();
        clickOn(REGISTER_STARTING_BALANCE_FIELD_ID).write(FIVE_HUNDRED.toString());
        clickOn(REGISTER_BUTTON_ID);

        try {
            Assertions.assertTrue(userHasBalance(FIVE_HUNDRED));
        }
        catch (EntryNotFoundException e) {

            fail("Did not create user with custom balance");
        }

        testStatementEntryIsCreatedWithAmount(FIVE_HUNDRED);
        TestService.deleteTestUser(SECOND_USERNAME);
    }

    @Test
    void testLoggingInAfterRegistration() throws Exception {

        enterCorrectCredentials();
        clickOn(REGISTER_BUTTON_ID);
        assertEquals(MAIN_WINDOW_TITLE, stage.getTitle());
        TestService.deleteTestUser(SECOND_USERNAME);
    }

    private void enterCorrectCredentials() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(SECOND_USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(PASSWORD);
    }

    private Boolean userHasBalance(Double balance) throws Exception {

        User newUser = getNewUser();
        return newUser.getBalance().equals(balance);
    }

    private void testStatementEntryIsCreatedWithAmount(Double amount) throws Exception {

        User newUser = getNewUser();
        List<StatementEntry> statementEntries =
                statementEntryTableProcedures.findEntriesForUser(newUser.getId());
        Assertions.assertEquals(1, statementEntries.size());

        StatementEntry balanceEntry = statementEntries.get(0);
        Assertions.assertEquals(BALANCE, balanceEntry.getType());
        Assertions.assertEquals(STARTING_BALANCE, balanceEntry.getTitle());
        Assertions.assertEquals(amount, balanceEntry.getAmount());
    }

    private User getNewUser() throws Exception {

        return new UserTableProcedures().findByUsername(SECOND_USERNAME);
    }
}