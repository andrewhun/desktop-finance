package com.andrewhun.finance.util;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.TestFxBaseClass;
import static com.andrewhun.finance.util.GuiElementIds.*;
import static com.andrewhun.finance.util.NamedConstants.*;

public class SceneSwitcherTest extends TestFxBaseClass {

    @AfterEach
    void logoutUser() throws Exception {

        userService.logoutUser(testUser);
        super.tearDown();
    }

    @Test
    void testThatWelcomePaneIsShown() {

        Assertions.assertEquals(WELCOME_PANE_TITLE, stage.getTitle());
    }

    @Test
    void testSwitchingToMainWindowWhenLoggingIn() {

        clickOn(LOGIN_USERNAME_FIELD_ID).write(USERNAME);
        clickOn(LOGIN_PASSWORD_FIELD_ID).write(PASSWORD);
        clickOn(LOGIN_BUTTON_ID);
        Assertions.assertEquals(MAIN_WINDOW_TITLE, stage.getTitle());
    }
}
