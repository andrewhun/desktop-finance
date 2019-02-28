package com.andrewhun.finance.welcomepane;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.TestFxBaseClass;
import static org.testfx.api.FxAssert.verifyThat;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.exceptions.EntryNotFoundException;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

class LoginTabPageControllerTest extends TestFxBaseClass {

    @AfterEach
    void logoutUser() throws Exception {

        userService.logoutUser(testUser);
        super.tearDown();
    }

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

        clickOn(LOGIN_USERNAME_FIELD_ID).write(INCORRECT_INPUT);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID,
                hasText(INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    void testIncorrectPassword() {
        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(INCORRECT_INPUT);
        clickOn(LOGIN_BUTTON_ID);
        verifyThat(LOGIN_ERROR_LABEL_ID,
                hasText(INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    void testLoggingIn() throws Exception {

        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);

        try {
            User loggedInUser = userService.getCurrentUser();
            Assertions.assertEquals(USERNAME, loggedInUser.getUsername());
        }
        catch (EntryNotFoundException e) {

            fail("Failed to log user in");
        }

        Assertions.assertEquals(MAIN_WINDOW_TITLE, stage.getTitle());
    }
}
