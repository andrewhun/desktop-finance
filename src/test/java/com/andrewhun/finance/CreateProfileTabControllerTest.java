/*
* This file contains the CreateProfileTabControllerTest class, which is
* responsible for testing the functionality of the create profile (alias
* register) tab on the welcome pane. This includes testing the reactions
* of the application to different kinds of invalid and valid inputs.
 */

package com.andrewhun.finance;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import com.andrewhun.finance.models.User;
import static org.testfx.api.FxAssert.verifyThat;
import static org.junit.jupiter.api.Assertions.fail;
import com.andrewhun.finance.exceptions.UserNotFoundException;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

class CreateProfileTabControllerTest extends TestFxBaseClass {

    private final String PASSWORD_MISMATCH_MESSAGE = "Password and its confirmation do not match!";
    private final String SECOND_USERNAME = "test2";

    @BeforeEach
    void setUpRegisterTab() {

        clickOn(REGISTER_TAB_ID);
    }

    @Test
    void testAllFieldsLeftEmpty() {

        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testOnlyUsernameIsFilled() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write("a");
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testConfirmationIsLeftEmpty() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write("a");
        clickOn(REGISTER_PASSWORD_FIELD_ID).write("b");
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testUsernameIsLeftEmpty() {

        clickOn(REGISTER_PASSWORD_FIELD_ID).write("b");
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write("b");
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testRequiredFieldsAreFilled() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write("a");
        clickOn(REGISTER_PASSWORD_FIELD_ID).write("b");
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write("c");
        clickOn(REGISTER_BUTTON_ID);
        // Should not show the "required field empty" error message
        verifyThat(REGISTER_ERROR_LABEL_ID, hasText(PASSWORD_MISMATCH_MESSAGE));
    }

    @Test
    void testPasswordMismatch() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(SECOND_USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write("nope");
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
                hasText("The selected username is already taken!"));
    }

    @Test
    void testInvalidBalanceInput() {

        enterCorrectCredentials();
        clickOn(REGISTER_STARTING_BALANCE_FIELD_ID).write("NaN");
        clickOn(REGISTER_BUTTON_ID);
        verifyThat(REGISTER_ERROR_LABEL_ID,
                hasText("Please enter a valid number for your starting balance!"));
    }

    @Test
    void testCreateUserWithDefaultBalance() throws SQLException {
        final Double DEFAULT_BALANCE = 10000.00;
        enterCorrectCredentials();
        clickOn(REGISTER_BUTTON_ID);
        sleep(1000);

        try {
            Assertions.assertTrue(userHasBalance(DEFAULT_BALANCE));
        }
        catch (UserNotFoundException e) {

            fail("Did not create user with default balance");
        }
        // Remove user to allow the reuse of the same username
        testProcedures.removeTestUserFromDatabase(SECOND_USERNAME);
    }

    @Test
    void testCreateUserWithCustomBalance() throws SQLException {
        final Double CUSTOM_BALANCE = 250.00;
        enterCorrectCredentials();
        clickOn(REGISTER_STARTING_BALANCE_FIELD_ID).write(CUSTOM_BALANCE.toString());
        clickOn(REGISTER_BUTTON_ID);

        try {
            Assertions.assertTrue(userHasBalance(CUSTOM_BALANCE));
        }
        catch (UserNotFoundException e) {

            fail("Did not create user with custom balance");
        }

        testProcedures.removeTestUserFromDatabase(SECOND_USERNAME);
    }

    private void enterCorrectCredentials() {

        clickOn(REGISTER_USERNAME_FIELD_ID).write(SECOND_USERNAME);
        clickOn(REGISTER_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(REGISTER_CONFIRMATION_FIELD_ID).write(PASSWORD);
    }

    private Boolean userHasBalance(Double balance) throws SQLException, UserNotFoundException {

        User newUser = userTableProcedures.getUserByUsername(SECOND_USERNAME);
        return newUser.getBalance().equals(balance);
    }
}
