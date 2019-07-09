/*
* This file contains the HistoryTabPageController class, which is
* responsible for handling user interactions on the history tab of
* the main window. This includes processing input from the "add
* statement entry" form, displaying the most recent information in all
* tables that are on this tab, and dealing with user actions regarding the
* statement entries table (editing and deleting selected entries, deleting all entries).
 */

package com.andrewhun.finance.mainwindow;

import java.util.*;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.andrewhun.finance.util.*;
import com.andrewhun.finance.services.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.uiaction.PageReloader;
import com.andrewhun.finance.models.StatementEntry;
import com.andrewhun.finance.uiaction.UIActionFactory;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.forminputprocessor.FormController;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;
import com.andrewhun.finance.databaseprocedures.TransactionTableProcedures;
import com.andrewhun.finance.databaseprocedures.StatementEntryTableProcedures;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

public class HistoryTabPageController implements  ModifiableEntriesTableController<StatementEntry>,
        FormController, PageReloader {

    private StatementEntryTableProcedures
            statementEntryTableProcedures = new StatementEntryTableProcedures();

    private NumericInputProcessor processor = new NumericInputProcessor(new AmountInputValidationService());

    private UserTableProcedures userTableProcedures = new UserTableProcedures();

    @FXML ChoiceBox<String> statementEntryType;
    @FXML TextField statementEntryTitle;
    @FXML TextField statementEntryAmount;
    @FXML Label addStatementEntryError;

    @FXML TableView<StatementEntry> statementEntriesTable;
    @FXML Label statementEntriesTableError;

    @FXML TableColumn<StatementEntry, String> statementEntryIdCol;
    @FXML TableColumn<StatementEntry, String> statementEntryTypeCol;
    @FXML TableColumn<StatementEntry, String> statementEntryTitleCol;
    @FXML TableColumn<StatementEntry, String> statementEntryAmountCol;
    @FXML TableColumn<StatementEntry, String> statementEntryTimeCol;

    @FXML private URL location;

    private ModifiableEntriesTableService<StatementEntry> entriesTableService =
            new ModifiableEntriesTableService<>(this, statementEntryTableProcedures);

    @FXML private void initialize() throws Exception {

        ChoiceBoxService choiceBoxService = new ChoiceBoxService();
        choiceBoxService.fillOutChoiceBox(statementEntryType, EntryAttribute.TYPE);

        entriesTableService.setUpEntriesTable();
    }

    public TableView<StatementEntry> getTable() {

        return statementEntriesTable;
    }

    public List<TableColumn<StatementEntry, String>> getListOfTableColumns() {

        List<TableColumn<StatementEntry, String>> tableColumns = new ArrayList<>();
        tableColumns.add(statementEntryIdCol);
        tableColumns.add(statementEntryTypeCol);
        tableColumns.add(statementEntryTitleCol);
        tableColumns.add(statementEntryAmountCol);
        tableColumns.add(statementEntryTimeCol);
        return tableColumns;
    }

    public List<String> getListOfFieldNames() {

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("id");
        fieldNames.add("type");
        fieldNames.add("title");
        fieldNames.add("amount");
        fieldNames.add("time");
        return fieldNames;
    }

    public void deleteSelectedStatementEntry() throws Exception {

        entriesTableService.handleModificationRequest(TableAction.DELETE);
    }

    @Override
    public void carryOutTasksAfterDeletingSelectedEntry(StatementEntry selectedEntry) throws Exception {

        User currentUser = userTableProcedures.findLoggedInUser();
        Double balance = currentUser.getBalance();
        balance = BalanceUtil.modifyBalanceWhenStatementEntryIsDeleted(balance, selectedEntry);
        currentUser.updateBalance(balance);
    }

    @Override
    public Boolean entryCanBeDeleted(StatementEntry statementEntry) {

        return (statementEntry != null && !statementEntry.hasType(BALANCE));
    }

    public void deleteAllStatementEntriesForUser() throws Exception {

        entriesTableService.deleteAllEntriesForCurrentUser();
    }

    @Override
    public Boolean userHasConfirmedDeletingAllEntries() {

        Optional<ButtonType> result = createAlert().showAndWait();
        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    // Source for confirmation dialog: https://code.makery.ch/blog/javafx-dialogs-official/
    private Alert createAlert() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account reset confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Your are about to delete the financial history, the stock portfolio" +
                "and the transaction history of your account. Your account will be emptied, " +
                "aside from the recurring entries you have set. Your starting balance will be reset" +
                "to 10,000.00 USD. Do you wish to proceed?");

        return alert;
    }

    @Override
    public void carryOutTasksAfterDeletingAllEntries() throws Exception {

        User currentUser = userTableProcedures.findLoggedInUser();
        currentUser.updateBalance(DEFAULT_BALANCE);
        createStartingBalanceEntry();
    }

    private void createStartingBalanceEntry() throws Exception {

        StatementEntry balanceEntry =
                StatementEntry.createIncompleteEntry(BALANCE, STARTING_BALANCE,
                        DEFAULT_BALANCE, UserUtil.getCurrentUserId());
        statementEntryTableProcedures.addEntryToDatabase(balanceEntry);
        deleteAllTransactionsForUser();
    }

    private void deleteAllTransactionsForUser() throws Exception {

        new TransactionTableProcedures().deleteAllTransactionsForUser(UserUtil.getCurrentUserId());
    }

    public void showEditStatementEntryForm() throws Exception {

         entriesTableService.handleModificationRequest(TableAction.EDIT);
    }

    public String getEditFormPath() {

        return "/fxml/EditStatementEntryForm.fxml";
    }

    public Label getEntriesTableError() {

        return statementEntriesTableError;
    }

    public URL getLocation() {

        return location;
    }

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
    }

    public void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        StatementEntry incompleteEntry = setUpStatementEntry();
        statementEntryTableProcedures.addEntryToDatabase(incompleteEntry);
        changeUserBalance(incompleteEntry);
    }

    public void reflectChangesOnUserInterface(Stage stage) throws Exception {

        UIActionFactory.createAction(this).performAction(stage);
    }

    private StatementEntry setUpStatementEntry() throws Exception {

        String type = statementEntryType.getValue();
        String title = statementEntryTitle.getText();
        Double amount = processor.castToDouble(statementEntryAmount.getText());
        return StatementEntry.createIncompleteEntry(type, title, amount, UserUtil.getCurrentUserId());
    }

    private void changeUserBalance(StatementEntry statementEntry) throws Exception {

        User currentUser = userTableProcedures.findLoggedInUser();
        Double balance = currentUser.getBalance();
        balance = BalanceUtil.calculateBalanceWhenStatementEntryIsCreated(balance, statementEntry);
        currentUser.updateBalance(balance);
    }

    public void reactToIncorrectInput() {

        if (!inputIsCorrect()) {

            showErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
        }
    }

    public Boolean aRequiredFieldIsEmpty() {

        return (statementEntryType.getValue() == null ||
                statementEntryType.getValue().equals("") ||
                statementEntryTitle.getText().isEmpty() ||
                statementEntryAmount.getText().isEmpty());
    }

    public Boolean inputIsCorrect() {


        return processor.inputIsValid(statementEntryAmount.getText());
    }

    public void showErrorMessage(String message) {

        addStatementEntryError.setText(message);
    }

    public void clearErrorMessageDisplay() {

        addStatementEntryError.setText("");
    }

    public Boolean errorMessageDisplayIsEmpty() {

        return addStatementEntryError.getText().equals("");
    }
}