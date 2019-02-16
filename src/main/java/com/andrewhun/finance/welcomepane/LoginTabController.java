/*
* This file contains the LoginTabController class, which is responsible for
* interacting with users on the login tab of the welcome pane. Tasks that the
* class has including validating login credentials, logging in users and
* communicating with users.
 */
package com.andrewhun.finance.welcomepane;

import java.util.List;
import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.andrewhun.finance.models.User;
import javafx.scene.control.PasswordField;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import com.andrewhun.finance.exceptions.UserNotFoundException;
import com.andrewhun.finance.security.PasswordEncryptionService;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class LoginTabController {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    private WelcomePaneController welcomePaneController;

    private static UserTableProcedures userTableProcedures = new UserTableProcedures();

    void injectWelcomePaneController(WelcomePaneController welcomePaneController) {

        this.welcomePaneController = welcomePaneController;
    }

    public void validateLoginCredentials(ActionEvent buttonPush) {

        try {
            if (inputIsValid()) {
                loginUser();
                welcomePaneController.switchToMainWindow(buttonPush);
            } else {
                reactToInvalidCredentials();
            }
        }
        catch (SQLException | UserNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException e) {

            e.printStackTrace();
            showErrorMessage("An error has occurred. Please contact the developers of the application.");
        }
    }

    private Boolean inputIsValid() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {

        return (!aRequiredFieldIsEmpty() && usernameIsValid() && passwordIsCorrect());
    }

    private void loginUser() throws UserNotFoundException, SQLException {

        User currentUser = userTableProcedures.getUserByUsername(loginUsername.getText());
        currentUser.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(currentUser);
    }

    private void reactToInvalidCredentials() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {

        clearErrorMessageDisplay();

        if (aRequiredFieldIsEmpty()) {

            showErrorMessage("A required field was left empty!");
        }
        else if (!usernameIsValid()) {

            showErrorMessage("Invalid username!");
        }
        else if (!passwordIsCorrect()) {

            showErrorMessage("Incorrect password!");
        }
        else {

            showErrorMessage("Something has gone wrong with your input. Please try again!");
        }
    }

    private Boolean aRequiredFieldIsEmpty() {

        return (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty());
    }

    private Boolean usernameIsValid() throws SQLException {

        List<String> usernames = userTableProcedures.getListOfUsernames();
        return usernames.contains(loginUsername.getText());
    }

    private Boolean passwordIsCorrect() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {

        PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
        try {
            User selectedUser = userTableProcedures.getUserByUsername(loginUsername.getText());
            return passwordEncryptionService.authenticate(loginPassword.getText(), selectedUser.getPasswordHash(),
                    selectedUser.getHashSalt());
        }
        // This may happen if the password check is done before the username check
        catch (UserNotFoundException e) {

            return false;
        }
    }

    private void clearErrorMessageDisplay() {

        loginError.setText("");
    }

    private void  showErrorMessage(String message) {

        loginError.setText(message);
    }
}