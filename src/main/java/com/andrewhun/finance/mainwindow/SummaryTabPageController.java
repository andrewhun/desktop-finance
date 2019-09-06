/*
* This file contains the SummaryTabPageController class, which is responsible
* for handling user interactions on the summary tab of the main window. This
* includes processing input from the add recurring entry form, as well as
* showing appropriate and "fresh" information to the user in the summary
* and recurring entries tables. The class is also expected to present the
* "edit recurring entry" popup window to users when the "edit entry" button
* is clicked.
 */

package com.andrewhun.finance.mainwindow;

import java.net.URL;
import java.util.List;

import com.andrewhun.finance.models.User;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.andrewhun.finance.services.*;
import com.andrewhun.finance.uiaction.PageReloader;
import com.andrewhun.finance.models.RecurringEntry;
import com.andrewhun.finance.uiaction.UIActionFactory;
import com.andrewhun.finance.forminputprocessor.FormController;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;
import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

public class SummaryTabPageController implements ModifiableEntriesTableController<RecurringEntry>,
        FormController, PageReloader {

    @FXML private ChoiceBox<String> recurringEntryType;
    @FXML private TextField recurringEntryTitle;
    @FXML private TextField recurringEntryAmount;
    @FXML private ChoiceBox<String> recurringEntryFrequency;
    @FXML private Label addRecurringEntryError;
    @FXML private TableView<RecurringEntry> recurringEntriesTable;
    @FXML private Label recurringEntriesTableError;

    @FXML private TableColumn<RecurringEntry, String> recurringEntryIdCol;
    @FXML private TableColumn<RecurringEntry, String> recurringEntryTypeCol;
    @FXML private TableColumn<RecurringEntry, String> recurringEntryTitleCol;
    @FXML private TableColumn<RecurringEntry, String> recurringEntryAmountCol;
    @FXML private TableColumn<RecurringEntry, String> recurringEntryFrequencyCol;
    @FXML private TableColumn<RecurringEntry, String> recurringEntryTimeCol;

    @FXML private URL location;

    private NumericInputProcessor numericInputProcessor =
            new NumericInputProcessor(new AmountInputValidationService());

    private RecurringEntryTableProcedures recurringEntryTableProcedures
            = new RecurringEntryTableProcedures();

    private ModifiableEntriesTableService<RecurringEntry> entriesTableService =
            new ModifiableEntriesTableService<>(this, recurringEntryTableProcedures);

    @FXML
    private void initialize() throws Exception {

        setUpChoiceBoxes();

        entriesTableService.setUpEntriesTable();
    }

    private void setUpChoiceBoxes() {

        ChoiceBoxService choiceBoxService = new ChoiceBoxService();
        choiceBoxService.fillOutChoiceBox(recurringEntryType, EntryAttribute.TYPE);
        choiceBoxService.fillOutChoiceBox(recurringEntryFrequency, EntryAttribute.FREQUENCY);
    }

    public TableView<RecurringEntry> getTable() {

        return recurringEntriesTable;
    }

    public List<TableColumn<RecurringEntry, String>> getListOfTableColumns() {

        List<TableColumn<RecurringEntry, String>> tableColumns = new ArrayList<>();
        tableColumns.add(recurringEntryIdCol);
        tableColumns.add(recurringEntryTypeCol);
        tableColumns.add(recurringEntryTitleCol);
        tableColumns.add(recurringEntryAmountCol);
        tableColumns.add(recurringEntryFrequencyCol);
        tableColumns.add(recurringEntryTimeCol);
        return tableColumns;
    }

    public List<String> getListOfFieldNames() {

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("id");
        fieldNames.add("type");
        fieldNames.add("title");
        fieldNames.add("amount");
        fieldNames.add("frequency");
        fieldNames.add("time");
        return fieldNames;
    }

    public void deleteSelectedRecurringEntry() throws Exception {

        entriesTableService.handleModificationRequest(TableAction.DELETE);
    }

    public void deleteAllRecurringEntriesForUser() throws Exception {

        RecurringEntry.deleteAllEntriesForUser(User.getCurrentUserId());
        entriesTableService.reloadPage();
    }

    public void showEditRecurringEntryForm() throws Exception {

        entriesTableService.handleModificationRequest(TableAction.EDIT);
    }

    public String getEditFormPath() {

        return "/fxml/EditRecurringEntryForm.fxml";
    }

    public Label getEntriesTableError() {

        return recurringEntriesTableError;
    }

    public URL getLocation() {

        return location;
    }

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
    }

    @Override
    public void reflectChangesOnUserInterface(Stage stage) throws Exception {

        UIActionFactory.createAction(this).performAction(stage);
    }

    @Override
    public Boolean inputIsCorrect() {

        return numericInputProcessor.inputIsValid(recurringEntryAmount.getText());
    }

    @Override
    public void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        recurringEntryTableProcedures.addEntryToDatabase(setUpRecurringEntry());
    }

    private RecurringEntry setUpRecurringEntry() throws Exception {

        String type = recurringEntryType.getValue();
        String title = recurringEntryTitle.getText();
        Double amount = numericInputProcessor.castToDouble(recurringEntryAmount.getText());
        String frequency = recurringEntryFrequency.getValue();

        return RecurringEntry.createIncompleteEntry(type, title, amount, User.getCurrentUserId(), frequency);

    }

    @Override
    public void reactToIncorrectInput() {

        if (!numericInputProcessor.inputIsValid(recurringEntryAmount.getText())) {

            showErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
        }
    }

    @Override
    public Boolean aRequiredFieldIsEmpty() {

        return (recurringEntryType.getValue() == null ||
                recurringEntryType.getValue().equals("") ||
                recurringEntryAmount.getText().isEmpty() ||
                recurringEntryTitle.getText().isEmpty() ||
                recurringEntryFrequency.getValue() == null ||
                recurringEntryFrequency.getValue().equals(""));
    }

    @Override
    public void clearErrorMessageDisplay() {

        addRecurringEntryError.setText("");
    }

    @Override
    public void showErrorMessage(String message) {

        addRecurringEntryError.setText(message);
    }

    @Override
    public Boolean errorMessageDisplayIsEmpty() {

        return (addRecurringEntryError.getText().equals(""));
    }
}