/*
* This file contains the RegisterTabPageController class, which is responsible for
* interacting users on the register tab of the welcome pane. Tasks
* that the class has include validating user input, communicating with users,
* initiating the creation of new rows in the Users table of the database and logging in
* the new user.
 */

package com.andrewhun.finance.welcomepane;

import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.andrewhun.finance.models.User;
import javafx.scene.control.PasswordField;
import com.andrewhun.finance.uiaction.PageChanger;
import com.andrewhun.finance.models.StatementEntry;
import com.andrewhun.finance.uiaction.UIActionFactory;
import static com.andrewhun.finance.util.ErrorMessages.*;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.services.NumericInputProcessor;
import com.andrewhun.finance.forminputprocessor.FormController;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.services.BalanceInputValidationService;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;

public class RegisterTabPageController implements FormController, PageChanger {

    @FXML private TextField registerUsername;
    @FXML private PasswordField registerPassword;
    @FXML private PasswordField registerConfirmation;
    @FXML private TextField startingBalance;
    @FXML private Label registerError;
    @FXML private Button registerButton;

    private UserTableProcedures userTableProcedures = new UserTableProcedures();
    private NumericInputProcessor numericInputProcessor =
            new NumericInputProcessor(new BalanceInputValidationService());

    /*@FXML private void initialize() {

        EventHandler<KeyEvent> commonHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                System.out.println("Button released: " + event.getText());

                if(registerUsername.getText().isEmpty() ||
                        registerPassword.getText().isEmpty()||
                registerConfirmation.getText().isEmpty() ||
                registerPassword.getText().length() < 8 ||
                registerConfirmation.getText().length() <8 ||
                !registerPassword.getText().equals(registerConfirmation.getText())) {
                    return;
            }
                registerButton.setDisable(false);
        }
        };

        registerUsername.addEventHandler(KeyEvent.KEY_RELEASED, commonHandler);
        registerPassword.addEventHandler(KeyEvent.KEY_TYPED, commonHandler);
        registerConfirmation.addEventHandler(KeyEvent.KEY_TYPED, commonHandler);
    }*/

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
    }

    @Override
    public void reflectChangesOnUserInterface(Stage stage) throws Exception {

        UIActionFactory.createAction(this).performAction(stage);
    }

    @Override
    public Boolean inputIsCorrect() throws Exception {

        return (passwordsMatch() && !usernameIsTaken() && balanceIsValid());
    }

    @Override
    public Boolean aRequiredFieldIsEmpty() {

        return (registerUsername.getText().isEmpty() || registerPassword.getText().isEmpty()
                || registerConfirmation.getText().isEmpty());
    }

    private Boolean passwordsMatch() {

        return (registerPassword.getText().equals(registerConfirmation.getText()));
    }

    private Boolean usernameIsTaken() throws SQLException {

        List<String> usernames = userTableProcedures.getListOfUsernames();
        return usernames.contains(registerUsername.getText());
    }

    private Boolean balanceIsValid() {

        return (startingBalanceIsEmpty() ||
                numericInputProcessor.inputIsValid(startingBalance.getText()));
    }

    private Boolean startingBalanceIsEmpty() {

        return (startingBalance.getText().isEmpty());
    }

    @Override
    public void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        String username = registerUsername.getText();
        String password = registerPassword.getText();
        Double balance = getBalance();

        User incompleteUser = User.createIncompleteEntry(username, password, balance);
        userTableProcedures.addEntryToDatabase(incompleteUser);
        User newUser = userTableProcedures.findByUsername(username);
        newUser.login();

        StatementEntry balanceEntry = StatementEntry.createIncompleteEntry
                (BALANCE, STARTING_BALANCE, newUser.getBalance(), newUser.getId());
        balanceEntry.createDatabaseRecord();
    }

    private Double getBalance() {

        Double balance = DEFAULT_BALANCE;
        if (!startingBalanceIsEmpty()) {
            balance = numericInputProcessor.castToDouble(startingBalance.getText());
        }

        return balance;
    }

    @Override
    public void reactToIncorrectInput() throws SQLException {

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
    public void clearErrorMessageDisplay() {

        registerError.setText("");
    }

    @Override
    public void showErrorMessage(String errorMessage) {

        registerError.setText(errorMessage);
    }

    @Override
    public Boolean errorMessageDisplayIsEmpty() {

        return (registerError.getText().equals(""));
    }
}