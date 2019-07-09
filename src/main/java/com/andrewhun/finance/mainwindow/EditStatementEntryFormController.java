/*
 * This file contains the EditStatementEntryFormController class, which is responsible
 * for handling user interactions on the "Edit statement entry" popup window.
 */

package com.andrewhun.finance.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.andrewhun.finance.services.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.util.BalanceUtil;
import com.andrewhun.finance.models.StatementEntry;
import static com.andrewhun.finance.util.NamedConstants.BALANCE;
import com.andrewhun.finance.forminputprocessor.EditFormController;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;
import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;

public class EditStatementEntryFormController implements EditFormController<StatementEntry> {

    @FXML private ChoiceBox<String> editStatementEntryType;
    @FXML private TextField editStatementEntryTitle;
    @FXML private TextField editStatementEntryAmount;
    @FXML private Label editStatementEntryError;

    private NumericInputProcessor amountInputProcessor =
            new NumericInputProcessor(new AmountInputValidationService());

    private StatementEntry originalEntry;

    @FXML private void initialize() {

        ChoiceBoxService choiceBoxService = new ChoiceBoxService();
        choiceBoxService.fillOutChoiceBox(editStatementEntryType, EntryAttribute.TYPE);
    }

    @Override
    public void injectEntry(StatementEntry entry) {

        this.originalEntry = entry;
    }

    @Override
    public void setUpFormForBalanceEntry(StatementEntry selectedEntry) {

        if (selectedEntry.hasType(BALANCE)) {

            editStatementEntryType.setDisable(true);
            editStatementEntryTitle.setDisable(true);
        }
    }

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
    }

    public void makeChangesToDatabase(StatementEntry editedEntry) throws Exception {

        new StatementEntryTableProcedures().editEntry(editedEntry);
        updateUserBalance(originalEntry, editedEntry);
    }

    private void updateUserBalance(StatementEntry originalEntry, StatementEntry editedEntry) throws Exception {

        User currentUser = new UserTableProcedures().findLoggedInUser();
        Double balance = currentUser.getBalance();
        balance = BalanceUtil.adjustBalanceWhenStatementEntryIsEdited(balance, originalEntry, editedEntry);
        currentUser.updateBalance(balance);
    }

    public Boolean aChangeWasMade() {

        return (typeWasChanged() || titleWasChanged() || amountWasChanged());
    }

    public Boolean typeWasChanged() {

        return !(editStatementEntryType.getValue() == null ||
                editStatementEntryType.getValue().equals(""));
    }

    public Boolean titleWasChanged() {

        return !(editStatementEntryTitle.getText().isEmpty());
    }

    public Boolean amountIsValid() {

        return (amountInputProcessor.inputIsValid(editStatementEntryAmount.getText()));
    }

    public Boolean amountWasChanged() {

        return !(editStatementEntryAmount.getText().isEmpty());
    }

    public void editType(StatementEntry editedEntry) {

        editedEntry.setType(editStatementEntryType.getValue());
    }

    public void editTitle(StatementEntry editedEntry) {

        editedEntry.setTitle(editStatementEntryTitle.getText());
    }

    public void editAmount(StatementEntry editedEntry) {

        editedEntry.setAmount(amountInputProcessor.castToDouble(editStatementEntryAmount.getText()));
    }

    public StatementEntry copyOriginalEntry() {

        return new StatementEntry(originalEntry.getId(), originalEntry.getType(), originalEntry.getTitle(),
                originalEntry.getAmount(), originalEntry.getUserId(), originalEntry.getTime());
    }

    public void showErrorMessage(String message) {

        editStatementEntryError.setText(message);
    }
}