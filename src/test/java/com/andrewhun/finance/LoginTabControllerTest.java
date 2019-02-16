/*
* This file contains the LoginTabControllerTest class, which is responsible
* for testing the functionality of the login tab on the welcome pane.
* This includes testing the reactions of the tab to different kinds of
* valid and invalid input.
 */

package com.andrewhun.finance;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.andrewhun.finance.models.User;
import static org.testfx.api.FxAssert.verifyThat;
import static org.junit.jupiter.api.Assertions.fail;
import com.andrewhun.finance.exceptions.UserNotFoundException;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

class LoginTabControllerTest extends TestFxBaseClass {




    @Test
    void testAllFieldsLeftEmpty() {

        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testPasswordFieldLeftEmpty() {

        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testUsernameFieldLeftEmpty() {

        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID, hasText(REQUIRED_FIELD_EMPTY_MESSAGE));
    }

    @Test
    void testInvalidUsername() {

        clickOn(LOGIN_USERNAME_FIELD_ID).write("Nope");
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID,
                hasText("Invalid username!"));
    }

    @Test
    void testIncorrectPassword() {
        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write("nope");
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID,
                hasText("Incorrect password!"));
    }

    @Test
    void testLoggingIn() throws SQLException {

        final String TO_DO_LABEL_ID = "#toDoLabel";

        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);

        try {
            User loggedInUser = userTableProcedures.getLoggedInUser();
            Assertions.assertEquals(USERNAME, loggedInUser.getUsername());
        }
        catch (UserNotFoundException e) {

            fail("Failed to log user in");
        }

        verifyThat(TO_DO_LABEL_ID, hasText("TO DO!"));
    }
}
