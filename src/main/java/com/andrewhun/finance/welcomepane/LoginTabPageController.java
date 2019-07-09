/*
* This file contains the LoginTabPageController class, which is responsible for
* interacting with users on the login tab of the welcome pane. Tasks that the
* class has including validating login credentials, logging in users and
* communicating with users.
 */

package com.andrewhun.finance.welcomepane;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.andrewhun.finance.models.User;
import javafx.scene.control.PasswordField;
import com.andrewhun.finance.uiaction.PageChanger;
import com.andrewhun.finance.uiaction.UIActionFactory;
import com.andrewhun.finance.exceptions.EntryNotFoundException;
import com.andrewhun.finance.forminputprocessor.FormController;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_CREDENTIALS_MESSAGE;

public class LoginTabPageController implements FormController, PageChanger {

    @FXML private TextField loginUsername;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    private UserTableProcedures userTableProcedures = new UserTableProcedures();
    private User user;

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
    }

    @Override
    public void reflectChangesOnUserInterface(Stage stage) throws Exception {

        UIActionFactory.createAction(this).performAction(stage);
    }

    @Override
    public Boolean inputIsCorrect() throws Exception {

        try {
            user = userTableProcedures.findByUsername(loginUsername.getText());
            return user.authenticate(loginPassword.getText());
        }
        catch (EntryNotFoundException e) {

            return false;
        }
    }

    @Override
    public void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        user.login();
    }

    @Override
    public void reactToIncorrectInput() {

        showErrorMessage(INVALID_CREDENTIALS_MESSAGE);
    }

    @Override
    public Boolean aRequiredFieldIsEmpty() {

        return (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty());
    }

    @Override
    public void clearErrorMessageDisplay() {

        loginError.setText("");
    }

    @Override
    public void showErrorMessage(String message) {

        loginError.setText(message);
    }

    @Override
    public Boolean errorMessageDisplayIsEmpty() {

        return (loginError.getText().equals(""));
    }
}