package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.database.DatabaseExtension;
import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.user.UserMapper;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DatabaseExtension.class)
public class LoginPageIntegrationTest extends ApplicationTest {

    private Stage stage;
    private final UserMapper userMapper = new UserMapper();

    @BeforeAll
    static void setUpHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @BeforeAll
    static void seedUser() throws Exception {
        new UserMapper().save(User.register("alice", "correctPass", BigDecimal.valueOf(10000)));
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        loadLoginPage();
        stage.show();
    }

    @BeforeEach
    void setUp() {
        reloadLoginPage();
    }

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

    // ==================== Empty Field Tests ====================

    @Test
    @DisplayName("Should show username-specific error when username is empty")
    void testEmptyUsername() {

        clickOn("#passwordField").write("password123");
        clickOn("#loginButton");
        assertEquals("Username is required", getErrorLabelText());
        assertTrue(fieldHasError("#usernameField"));
    }

    @Test
    @DisplayName("Should show password-specific error when password is empty")
    void testEmptyPassword() {

        clickOn("#usernameField").write("alice");
        clickOn("#loginButton");
        assertEquals("Password is required", getErrorLabelText());
        assertTrue(fieldHasError("#passwordField"));
    }

    @Test
    @DisplayName("Should report the username first when both fields are empty")
    void testBothFieldsEmpty() {
        clickOn("#loginButton");
        assertEquals("Username is required", getErrorLabelText());
    }

    @Test
    @DisplayName("Should treat whitespace-only input as empty")
    void testWhitespaceOnlyUsername() {

        clickOn("#usernameField").write("   ");
        clickOn("#passwordField").write("password123");
        clickOn("#loginButton");
        assertEquals("Username is required", getErrorLabelText());
    }

    @Test
    @DisplayName("Should highlight a required field when it loses focus while empty")
    void testFieldHighlightedOnBlurWhenEmpty() {
        clickOn("#usernameField");
        clickOn("#passwordField");
        assertTrue(fieldHasError("#usernameField"));
    }

    @Test
    @DisplayName("Should clear a field's highlight when it regains focus")
    void testHighlightClearedOnRefocus() {

        clickOn("#usernameField");
        clickOn("#passwordField");
        clickOn("#usernameField");
        assertFalse(fieldHasError("#usernameField"));
    }

    // ==================== Invalid Credential Tests ====================

    @Test
    @DisplayName("Should show error when username does not exist")
    void testUnknownUsername() {

        clickOn("#usernameField").write("nobody");
        clickOn("#passwordField").write("password123");
        clickOn("#loginButton");
        assertEquals("Invalid credentials!", getErrorLabelText());
    }

    @Test
    @DisplayName("Should show error when password is incorrect")
    void testWrongPassword() {

        clickOn("#usernameField").write("alice");
        clickOn("#passwordField").write("wrongPass");
        clickOn("#loginButton");
        assertEquals("Invalid credentials!", getErrorLabelText());
    }

    // ==================== Navigation Tests ====================

    @Test
    @DisplayName("Should navigate to register page when register link is clicked")
    void testRegisterLinkNavigation() {
        clickOn("#registerLink");
        assertEquals("Create Account", stage.getTitle());
    }

    @Test
    @DisplayName("Should navigate to main window on successful login")
    void testSuccessfulLoginNavigatesToMainWindow() {

        clickOn("#usernameField").write("alice");
        clickOn("#passwordField").write("correctPass");
        clickOn("#loginButton");
        assertEquals("Desktop Finance", stage.getTitle());
    }

    @Test
    @DisplayName("Should mark user as logged in after successful login")
    void testSuccessfulLoginSetsUserLoggedIn() throws SQLException {

        clickOn("#usernameField").write("alice");
        clickOn("#passwordField").write("correctPass");
        clickOn("#loginButton");
        assertTrue(userMapper.findByUsername("alice").orElseThrow().isLoggedIn());
    }

    private String getErrorLabelText() {
        return ((Label) lookup("#errorLabel").query()).getText();
    }

    private boolean fieldHasError(String selector) {
        return lookup(selector).query().getStyleClass().contains("error");
    }
}