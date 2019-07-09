/*
* This file contains the EditRecurringEntryFormController class, which is responsible
* for handling user interactions on the "Edit recurring entry" popup window.
 */

package com.andrewhun.finance.mainwindow;

import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import com.andrewhun.finance.services.*;
import com.andrewhun.finance.models.RecurringEntry;
import com.andrewhun.finance.forminputprocessor.EditFormController;
import com.andrewhun.finance.forminputprocessor.FormInputProcessorFactory;
import com.andrewhun.finance.databaseprocedures.RecurringEntryTableProcedures;

public class EditRecurringEntryFormController implements EditFormController<RecurringEntry> {

    @FXML ChoiceBox<String> editRecurringEntryType;
    @FXML TextField editRecurringEntryTitle;
    @FXML TextField editRecurringEntryAmount;
    @FXML ChoiceBox<String> editRecurringEntryFrequency;
    @FXML Label editRecurringEntryError;

    private NumericInputProcessor amountInputProcessor =
            new NumericInputProcessor(new AmountInputValidationService());
    private RecurringEntry originalEntry;


    @FXML private void initialize() {

        setUpChoiceBoxes();
    }

    private void setUpChoiceBoxes() {

        ChoiceBoxService choiceBoxService = new ChoiceBoxService();
        choiceBoxService.fillOutChoiceBox(editRecurringEntryType, EntryAttribute.TYPE);
        choiceBoxService.fillOutChoiceBox(editRecurringEntryFrequency, EntryAttribute.FREQUENCY);
    }

   @Override
   public void editUniqueFields(RecurringEntry editedEntry) {

       if (frequencyWasChanged()) {

           editedEntry.setFrequency(editRecurringEntryFrequency.getValue());
       }
   }

   public void processFormInput(ActionEvent buttonPush) throws Exception {

       FormInputProcessorFactory.createProcessor(this).processFormInput(buttonPush);
   }

   public void injectEntry(RecurringEntry entry) {

        this.originalEntry = entry;
   }

   public void makeChangesToDatabase(RecurringEntry editedEntry) throws SQLException {

       new RecurringEntryTableProcedures().editEntry(editedEntry);
   }

    public Boolean aChangeWasMade() {

        return (typeWasChanged() || titleWasChanged() || amountWasChanged() || frequencyWasChanged());
    }

    public Boolean typeWasChanged() {

        return !(editRecurringEntryType.getValue() == null ||
                editRecurringEntryType.getValue().equals(""));
    }

    public Boolean titleWasChanged() {

        return !(editRecurringEntryTitle.getText().isEmpty());
    }

    public Boolean amountIsValid() {

        return (amountInputProcessor.inputIsValid(editRecurringEntryAmount.getText()));
    }

    public Boolean amountWasChanged() {

        return !(editRecurringEntryAmount.getText().isEmpty());
    }

    private Boolean frequencyWasChanged() {

        return !(editRecurringEntryFrequency.getValue() == null ||
                editRecurringEntryFrequency.getValue().equals(""));
    }

    public void editType(RecurringEntry entry) {

        entry.setType(editRecurringEntryType.getValue());
    }

    public void editTitle(RecurringEntry entry) {

        entry.setTitle(editRecurringEntryTitle.getText());
    }

    public void editAmount(RecurringEntry entry) {

        Double editedAmount = amountInputProcessor.castToDouble(editRecurringEntryAmount.getText());
        entry.setAmount(editedAmount);
    }

    public RecurringEntry copyOriginalEntry() {

        return new RecurringEntry(originalEntry.getId(), originalEntry.getType(),
                originalEntry.getTitle(), originalEntry.getAmount(),
                originalEntry.getUserId(), originalEntry.getTime(), originalEntry.getFrequency());
    }

    public void showErrorMessage(String message) {

        editRecurringEntryError.setText(message);
    }
}