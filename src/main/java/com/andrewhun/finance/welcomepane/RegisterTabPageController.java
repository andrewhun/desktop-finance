/*
* This file contains the RegisterTabPageController class, which is responsible for
* interacting users on the register tab of the welcome pane. Tasks
* that the class has include validating user input, communicating with users,
* initiating the creation of new rows in the Users table of the database and logging in
* the new user.
 */

package com.andrewhun.finance.welcomepane;

import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.andrewhun.finance.models.User;
import javafx.scene.control.PasswordField;
import com.andrewhun.finance.FormInputProcessor;
import com.andrewhun.finance.services.UserService;
import static com.andrewhun.finance.util.ErrorMessages.*;
import com.andrewhun.finance.services.NumericInputProcessor;
import com.andrewhun.finance.services.BalanceInputValidationService;
import static com.andrewhun.finance.util.NamedConstants.DEFAULT_BALANCE;

public class RegisterTabPageController extends FormInputProcessor {

    private WelcomePaneController welcomePaneController;

    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmation;
    @FXML private TextField startingBalance;
    @FXML private Label registerError;

    private UserService userService = new UserService();
    private NumericInputProcessor numericInputProcessor =
            new NumericInputProcessor(new BalanceInputValidationService());

    void injectWelcomePaneController(WelcomePaneController welcomePaneController) {

        this.welcomePaneController = welcomePaneController;
    }

    @Override
    protected Boolean inputIsCorrect() throws Exception {

        return (passwordsMatch() && !usernameIsTaken() && balanceIsValid());
    }

    @Override
    protected Boolean aRequiredFieldIsEmpty() {

        return (registerUsername.getText().isEmpty() || registerPassword.getText().isEmpty()
                || registerConfirmation.getText().isEmpty());
    }

    private Boolean passwordsMatch() {

        return (registerPassword.getText().equals(registerConfirmation.getText()));
    }

    private Boolean usernameIsTaken() throws SQLException {

        return userService.usernameIsTaken(registerUsername.getText());
    }

    private Boolean balanceIsValid() {

        return (startingBalanceIsEmpty() ||
                numericInputProcessor.inputIsValid(startingBalance.getText()));
    }

    private Boolean startingBalanceIsEmpty() {

        return (startingBalance.getText().isEmpty());
    }

    @Override
    protected void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        String username = registerUsername.getText();
        String password = registerPassword.getText();
        Double balance = getBalance();
        userService.addUserToDatabase(username, password, balance);

        User newUser = userService.findByUsername(username);
        userService.loginUser(newUser);

        welcomePaneController.switchToMainWindow(buttonPush);
    }

    private Double getBalance() {

        Double balance = DEFAULT_BALANCE;
        if (!startingBalanceIsEmpty()) {
            balance = numericInputProcessor.castToDouble(startingBalance.getText());
        }

        return balance;
    }

    @Override
    protected void reactToIncorrectInput() throws SQLException {

        if (!passwordsMatch()) {
            showErrorMessage(PASSWORD_MISMATCH_MESSAGE);
        }
        else if (usernameIsTaken()) {
            showErrorMessage(USERNAME_TAKEN_MESSAGE);
        }
        else if (!balanceIsValid()) {
            showErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
        }
    }

    @Override
    protected void clearErrorMessageDisplay() {

        registerError.setText("");
    }

    @Override
    protected void showErrorMessage(String errorMessage) {

        registerError.setText(errorMessage);
    }

    @Override
    protected Boolean errorMessageDisplayIsEmpty() {

        return (registerError.getText().equals(""));
    }
}