/*
* This file contains the TestFXBaseClass, which serves as the base for all test classes
* that rely on TestFx to carry out their tests.
 */

package com.andrewhun.finance;

import javafx.stage.Stage;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import com.andrewhun.finance.models.User;
import org.testfx.framework.junit5.ApplicationTest;
import com.andrewhun.finance.databaseprocedures.ActiveDatabase;
import com.andrewhun.finance.databaseprocedures.TestProcedures;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.databaseprocedures.StoredProceduresBaseClass;

public class TestFxBaseClass extends ApplicationTest {

    static TestProcedures testProcedures = new TestProcedures();
    static UserTableProcedures userTableProcedures = new UserTableProcedures();
    private static User testUser;

    final String REGISTER_TAB_ID = "#physicalCreateProfileTab";
    final String REGISTER_USERNAME_FIELD_ID = "#registerUsername";
    final String REGISTER_PASSWORD_FIELD_ID = "#registerPassword";
    final String REGISTER_CONFIRMATION_FIELD_ID = "#registerConfirmation";
    final String REGISTER_STARTING_BALANCE_FIELD_ID = "#startingBalance";
    final String REGISTER_BUTTON_ID = "#registerButton";

    final String LOGIN_USERNAME_FIELD_ID = "#loginUsername";
    final String LOGIN_PASSWORD_FIELD_ID = "#loginPassword";
    final String LOGIN_BUTTON_ID = "#loginButton";

    final String REGISTER_ERROR_LABEL_ID = "#registerError";
    final String LOGIN_ERROR_LABEL_ID = "#loginError";
    final String REQUIRED_FIELD_EMPTY_MESSAGE = "A required field was left empty!";

    final String WELCOME_PANE_TITLE = "Welcome to Desktop Finance!";
    final String MAIN_WINDOW_TITLE = "Desktop Finance";

    static final String USERNAME = "test";
    final String PASSWORD = "testPass";

    @Override
    public void start(Stage stage) throws Exception {

        new App().start(stage);
    }

    @BeforeAll
    static void setUpTestDatabase() throws Exception {

        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        testProcedures.createUsersTable();
        testProcedures.addTestUserToDatabase(USERNAME);
        testUser = userTableProcedures.getUserByUsername(USERNAME);
    }

    @AfterEach
    void tearDown() throws Exception {

        testUser.setIsLoggedIn(false);
        userTableProcedures.changeUserLoginStatus(testUser);

        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @AfterAll
    static void removeTestData() throws SQLException {

        testProcedures.dropUsersTable();
    }
}
