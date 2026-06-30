package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.user.UserMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginPageController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserMapper userMapper = new UserMapper();

    @FXML
    void initialize() {
        addRequiredFieldValidation(usernameField);
        addRequiredFieldValidation(passwordField);
    }

    private void addRequiredFieldValidation(TextInputControl field) {

        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                clearError();
                field.getStyleClass().remove("error");
            }
            else if (field.getText().isBlank()) {
                markInvalid(field);
            }
        });
    }

    @FXML
    void handleLogin(ActionEvent event) {

        clearError();
        if (hasMissingRequiredField()) {
            return;
        }
        try {
            attemptLogin();
        }
        catch (SQLException | GeneralSecurityException | IOException e) {
            showError("An error has occurred. Please contact the developer(s).");
        }
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private boolean hasMissingRequiredField() {

        if (usernameField.getText().isBlank()) {
            showError("Username is required", usernameField);
            return true;
        }
        if (passwordField.getText().isBlank()) {
            showError("Password is required", passwordField);
            return true;
        }
        return false;
    }

    private void attemptLogin() throws SQLException, GeneralSecurityException, IOException {

        Optional<User> maybeUser = findAuthenticatedUser();
        if (maybeUser.isEmpty()) {
            showError("Invalid credentials!");
            return;
        }
        completeLogin(maybeUser.get());
    }

    private Optional<User> findAuthenticatedUser() throws SQLException, GeneralSecurityException {

        Optional<User> maybeUser = userMapper.findByUsername(usernameField.getText());
        if (maybeUser.isPresent() && maybeUser.get().authenticate(passwordField.getText())) {
            return maybeUser;
        }
        return Optional.empty();
    }

    private void completeLogin(User user) throws SQLException, IOException {
        user.login();
        userMapper.save(user);
        navigateToMainWindow();
    }

    private void navigateToMainWindow() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        WindowNavigator.showWindow(stage, Page.MAIN_WINDOW);
    }

    @FXML
    void handleRegisterLink(ActionEvent event) {

        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            WindowNavigator.showWindow(stage, Page.REGISTER);
        }
        catch (IOException e) {
            showError("An error has occurred. Please contact the developer(s).");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void showError(String message, TextInputControl field) {
        showError(message);
        markInvalid(field);
    }

    private void markInvalid(TextInputControl field) {
        if (!field.getStyleClass().contains("error")) {
            field.getStyleClass().add("error");
        }
    }
}