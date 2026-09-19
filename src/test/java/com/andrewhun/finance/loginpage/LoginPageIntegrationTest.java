package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;
import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.usermapper.UserMapper;
import com.andrewhun.finance.usermapper.UserMapperFactory;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPageIntegrationTest extends ApplicationTest {

    private Stage stage;

    @BeforeAll
    static void doBeforeAllSetUp() throws  Exception {
        setUpHeadlessMode();
        registerNewUser();
    }

    private static void setUpHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    private static void registerNewUser() throws Exception {
        AppContext.setStorageMode(StorageMode.IN_MEMORY);
        User user = User.register("alice", "correctPass", BigDecimal.valueOf(10000));
        UserMapperFactory.create().save(user);
    }

    @AfterAll
    static void resetStorage() {
        UserMapperFactory.reset();
        AppContext.reset();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        loadLoginPage();
        stage.show();
    }

    @BeforeEach
    void doBeforeEachSetUp() {
        reloadLoginPage();
    }

    /*
    * This method ensures that the Login Page is loaded on the JavaFX thread.
    * It relies on interact method of the FXRobot class.
    */
    private void reloadLoginPage() {

        interact(() -> {
            try {
                loadLoginPage();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadLoginPage() throws IOException {
        WindowNavigator.showWindow(stage, Page.LOGIN);
    }

    // ==================== Field Highlight Tests ====================

    @Test
    @DisplayName("Should highlight a required field when it loses focus while empty")
    void testHighlightingBlankField() {
        clickOn("#usernameField");
        clickOn("#passwordField");
        assertTrue(fieldHasError("#usernameField"));
    }

    @Test
    @DisplayName("Should clear a field's highlight when it regains focus")
    void testClearingFieldHighlight() {

        clickOn("#usernameField");
        clickOn("#passwordField");
        clickOn("#usernameField");
        assertFalse(fieldHasError("#usernameField"));
    }

    private boolean fieldHasError(String selector) {
        return lookup(selector).query().getStyleClass().contains("error");
    }

    // ==================== Navigation Tests ====================

    @Test
    @DisplayName("Should navigate to register page when register link is clicked")
    void testNavigatingToRegisterPage() {
        clickOn("#registerLink");
        assertEquals("Create Account", stage.getTitle());
    }

    @Test
    @DisplayName("Should mark user as logged in and navigate to main window on successful login")
    void testSuccessfulLogin() {

        clickOn("#usernameField").write("alice");
        clickOn("#passwordField").write("correctPass");
        clickOn("#loginButton");
        assertEquals("Desktop Finance", stage.getTitle());
        assertTrue(userIsLoggedIn("alice"));
    }

    private Boolean userIsLoggedIn(String username) {

        UserMapper userMapper = UserMapperFactory.create();
        Optional<User> optionalUser = userMapper.findByUsername(username);
        User user = optionalUser.orElseThrow();
        return user.isLoggedIn();
    }
}