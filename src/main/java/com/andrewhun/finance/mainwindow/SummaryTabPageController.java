/*
* This file contains the SummaryTabPageController class, which is responsible
* for handling user interactions on the summary tab of the main window. This
* includes processing input from the add recurring entry form, as well as
* showing appropriate and "fresh" information to the user in the summary
* and recurring entries tables.
 */

package com.andrewhun.finance.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.andrewhun.finance.services.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.FormInputProcessor;

import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

public class SummaryTabPageController extends FormInputProcessor {

    @FXML private ChoiceBox recurringEntryType;
    @FXML private TextField recurringEntryTitle;
    @FXML private TextField recurringEntryAmount;
    @FXML private ChoiceBox recurringEntryFrequency;
    @FXML private Button addRecurringEntryButton;
    @FXML private Label addRecurringEntryError;
    @FXML private TableView recurringEntriesTable;

    private ObservableList<String> recurringEntryTypeList =
            FXCollections.observableArrayList("", "Income", "Expense");

    private ObservableList<String> recurringEntryFrequencyList =
            FXCollections.observableArrayList("", "Daily", "Weekly", "Monthly");

    private NumericInputProcessor numericInputProcessor =
            new NumericInputProcessor(new AmountInputValidationService());

    @FXML
    private void initialize() {

        recurringEntryType.setItems(recurringEntryTypeList);
        recurringEntryFrequency.setItems(recurringEntryFrequencyList);
    }

    @Override
    protected Boolean inputIsCorrect() {

        return numericInputProcessor.inputIsValid(recurringEntryAmount.getText());
    }

    @Override
    protected void makeChangesToDatabase(ActionEvent buttonPush) throws Exception {

        String type = recurringEntryType.getValue().toString();
        String title = recurringEntryTitle.getText();
        Double amount = numericInputProcessor.castToDouble(recurringEntryAmount.getText());
        String frequency = recurringEntryFrequency.getValue().toString();
        User currentUser = new UserService().getCurrentUser();
        RecurringEntryService recurringEntryService = new RecurringEntryService();
        recurringEntryService.addRecurringEntryToDatabase(type, title, amount, currentUser.getId(), frequency);
    }

    @Override
    protected void reactToIncorrectInput() {

        if (!numericInputProcessor.inputIsValid(recurringEntryAmount.getText())) {

            showErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
        }
    }

    @Override
    protected Boolean aRequiredFieldIsEmpty() {

        return (recurringEntryType.getValue() == null ||
                recurringEntryType.getValue().toString().equals("") ||
                recurringEntryAmount.getText().isEmpty() ||
                recurringEntryTitle.getText().isEmpty() ||
                recurringEntryFrequency.getValue() == null ||
                recurringEntryFrequency.getValue().toString().equals(""));
    }

    @Override
    protected void clearErrorMessageDisplay() {

        addRecurringEntryError.setText("");
    }

    @Override
    protected void showErrorMessage(String message) {

        addRecurringEntryError.setText(message);
    }

    @Override
    protected Boolean errorMessageDisplayIsEmpty() {

        return (addRecurringEntryError.getText().equals(""));
    }
}