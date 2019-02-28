/*
* This file contains the LoginTabPageController class, which is responsible for
* interacting with users on the login tab of the welcome pane. Tasks that the
* class has including validating login credentials, logging in users and
* communicating with users.
 */

package com.andrewhun.finance.welcomepane;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.andrewhun.finance.models.User;
import javafx.scene.control.PasswordField;
import com.andrewhun.finance.FormInputProcessor;
import com.andrewhun.finance.services.UserService;
import com.andrewhun.finance.exceptions.EntryNotFoundException;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_CREDENTIALS_MESSAGE;


public class LoginTabPageController extends FormInputProcessor {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    private WelcomePaneController welcomePaneController;

    private UserService userService = new UserService();
    private User user;

    void injectWelcomePaneController(WelcomePaneController welcomePaneController) {

        this.welcomePaneController = welcomePaneController;
    }

    @Override
    protected Boolean inputIsCorrect() throws Exception {

        try {
            user = userService.findByUsername(loginUsername.getText());
            return userService.authenticate(user, loginPassword.getText());
        }
        catch (EntryNotFoundException e) {

            return false;
        }
    }

    @Override
    protected void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        userService.loginUser(user);
        welcomePaneController.switchToMainWindow(buttonPush);
    }

    @Override
    protected void reactToIncorrectInput() {

        showErrorMessage(INVALID_CREDENTIALS_MESSAGE);
    }

    @Override
    protected Boolean aRequiredFieldIsEmpty() {

        return (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty());
    }

    @Override
    protected void clearErrorMessageDisplay() {

        loginError.setText("");
    }

    @Override
    protected void showErrorMessage(String message) {

        loginError.setText(message);
    }

    @Override
    protected Boolean errorMessageDisplayIsEmpty() {

        return (loginError.getText().equals(""));
    }
}