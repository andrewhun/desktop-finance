/*
* This file contains the SceneSwitcherTest class, which is responsible for testing
* the mechanism that switches between the welcome pane and the main window of the application.
 */
package com.andrewhun.finance;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;

public class SceneSwitcherTest extends TestFxBaseClass {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        new App().start(stage);
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
