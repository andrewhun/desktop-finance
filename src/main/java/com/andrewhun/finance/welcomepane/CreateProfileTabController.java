/*
* This file contains the CreateProfileTabController class, which is responsible for
* interacting users on the create profile tab of the welcome pane. Tasks
* that the class has include validating user input, communicating with users,
* initiating the creation of new rows in the Users table of the database and logging in
* the new user.
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

public class CreateProfileTabController {

    private WelcomePaneController welcomePaneController;

    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmation;
    @FXML private TextField startingBalance;
    @FXML private Label registerError;

    private static UserTableProcedures userTableProcedures = new UserTableProcedures();

    void injectWelcomePaneController(WelcomePaneController welcomePaneController) {

        this.welcomePaneController = welcomePaneController;
    }

    public void validateUserInput(ActionEvent buttonPush) {

        try {
            if (inputIsValid()) {
                User newUser = createUserProfile();
                loginNewUser(newUser);
                welcomePaneController.switchToMainWindow(buttonPush);
            }
            else {
                reactToInvalidInput();
            }
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException | SQLException | UserNotFoundException e) {
            e.printStackTrace();
            showErrorMessage("An error has occurred. Please contact the developer(s) of the application.");
        }

    }

    private Boolean inputIsValid() throws SQLException {
        return (!aRequiredFieldIsEmpty() && passwordsMatch() && !usernameIsTaken() && balanceIsValid());
    }

    private User createUserProfile() throws NoSuchAlgorithmException, InvalidKeySpecException,
     SQLException, UserNotFoundException {

        byte[] salt = getSalt();
        byte[] passwordHash = getPasswordHash(registerPassword.getText(), salt);

        Double balance = 10000.00;
        if (!startingBalanceIsEmpty()) {
            try {
                balance = castBalanceToDouble();
            }
            catch(NumberFormatException e) {
                // This can only happen if invalid balance input "slips through" the validation
                // checks somehow (very unlikely)
                reactToInvalidInput();
            }
        }
        return createNewUser(registerUsername.getText(), passwordHash, balance, salt);
    }

    private void loginNewUser(User user) throws SQLException {

        user.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(user);
    }

    private void reactToInvalidInput() throws SQLException {

        clearErrorMessageDisplay();

        if (aRequiredFieldIsEmpty()) {
            showErrorMessage("A required field was left empty!");
        }
        else if (!passwordsMatch()) {
            showErrorMessage("Password and its confirmation do not match!");
        }
        else if (usernameIsTaken()) {
            showErrorMessage("The selected username is already taken!");
        }
        else if (!balanceIsValid()) {
            showErrorMessage("Please enter a valid number for your starting balance!");
        }
        else {
            showErrorMessage("Something has gone wrong with your input. Please try again!");
        }
    }

    private User createNewUser(String username, byte[] passwordHash, Double balance, byte[] salt)
            throws SQLException, UserNotFoundException {

        User incompleteUserModel = new User(username, passwordHash, balance, salt);
        userTableProcedures.addUserToDatabase(incompleteUserModel);
        return userTableProcedures.getUserByUsername(username);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {

        PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
        return passwordEncryptionService.generateSalt();
    }

    private byte[] getPasswordHash(String password, byte[] salt) throws NoSuchAlgorithmException,
     InvalidKeySpecException {

        PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
        return passwordEncryptionService.getEncryptedPassword(password, salt);
    }

    private void clearErrorMessageDisplay() {

        registerError.setText("");
    }

    private Boolean aRequiredFieldIsEmpty() {

        return (registerUsername.getText().isEmpty() || registerPassword.getText().isEmpty()
                || registerConfirmation.getText().isEmpty());
    }

    private Boolean passwordsMatch() {

        return (registerPassword.getText().equals(registerConfirmation.getText()));
    }

    private Boolean usernameIsTaken() throws SQLException {

        List<String> usernames = userTableProcedures.getListOfUsernames();
        return(usernames.contains(registerUsername.getText()));
    }

    private Boolean balanceIsValid() {

        // Handle the constant field values for Doubles
        if (balanceIsConstantFieldValue()) {
            return false;
        }

        return (startingBalanceIsEmpty() || balanceIsNumeric());
    }

    private Boolean balanceIsConstantFieldValue() {

        return (startingBalance.getText().equals("NaN") ||
                startingBalance.getText().equals("MAX_EXPONENT") ||
                startingBalance.getText().equals("MAX_VALUE") ||
                startingBalance.getText().equals("MIN_EXPONENT") ||
                startingBalance.getText().equals("MIN_NORMAL") ||
                startingBalance.getText().equals("MIN_VALUE") ||
                startingBalance.getText().equals("NEGATIVE_INFINITY") ||
                startingBalance.getText().equals("POSITIVE INFINITY"));
    }

    private Boolean startingBalanceIsEmpty() {

        return (startingBalance.getText().isEmpty());
    }

    private Boolean balanceIsNumeric() {

        try {
            castBalanceToDouble();
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private Double castBalanceToDouble() throws NumberFormatException {
        return (Double.parseDouble(startingBalance.getText()));
    }

    private void showErrorMessage(String errorMessage) {

        registerError.setText(errorMessage);
    }
}