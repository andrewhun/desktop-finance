package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final LoginService loginService = new LoginService();

    @FXML
    void initialize() {
        addRequiredFieldValidation(usernameField);
        addRequiredFieldValidation(passwordField);
    }

    private void addRequiredFieldValidation(TextInputControl field) {

        field.focusedProperty().addListener(
            (obs, wasFocused, isNowFocused) -> {
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
        LoginResult result = loginService.attemptLogin(usernameField.getText(), passwordField.getText());
        switch (result.getStatus()) {
            case USERNAME_REQUIRED -> showError("Username is required", usernameField);
            case PASSWORD_REQUIRED -> showError("Password is required", passwordField);
            case INVALID_CREDENTIALS -> showError("Invalid credentials!");
            case ERROR -> showError("An error has occurred. Please contact the developer(s).");
            case SUCCESS -> completeNavigation();
        }
    }

    private void completeNavigation() {

        try {
            navigateToMainWindow();
        }
        catch (IOException e) {
            showError("An error has occurred. Please contact the developer(s).");
        }
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
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

    private void showError(String message, TextInputControl field) {
        showError(message);
        markInvalid(field);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void markInvalid(TextInputControl field) {
        if (!field.getStyleClass().contains("error")) {
            field.getStyleClass().add("error");
        }
    }
}