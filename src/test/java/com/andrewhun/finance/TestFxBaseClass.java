/*
* This file contains the TestFXBaseClass, which serves as the base for all test classes
* that rely on TestFx to carry out their tests.
 */

package com.andrewhun.finance;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.services.UserService;
import com.andrewhun.finance.services.TestService;
import org.testfx.framework.junit5.ApplicationTest;

public class TestFxBaseClass extends ApplicationTest {

    protected static User testUser;
    protected static UserService userService = new UserService();
    protected Stage stage;


    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        new App().start(stage);
    }

    @BeforeAll
    public static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @AfterEach
    protected void tearDown() throws Exception {

        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}