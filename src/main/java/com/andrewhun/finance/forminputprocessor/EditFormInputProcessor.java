/*
This file contains the EditFormInputProcessor class, which encapsulates the logic of processing input from
forms that allow users to edit existing database records.
 */

package com.andrewhun.finance.forminputprocessor;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.andrewhun.finance.util.WindowUtil;

import static com.andrewhun.finance.util.ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE;

public class EditFormInputProcessor<T> implements FormInputProcessor {

    private EditFormController<T> controller;

    public EditFormInputProcessor(EditFormController<T> controller) {

        this.controller = controller;
    }

    public void processFormInput(ActionEvent buttonPush) throws Exception {

        if(controller.aChangeWasMade()) {
           T editedEntry = controller.copyOriginalEntry();

            try {
                editCommonFields(editedEntry);
            }
            catch (NumberFormatException e) {
                controller.showErrorMessage(INVALID_NUMERIC_INPUT_MESSAGE);
                return;
            }

            controller.editUniqueFields(editedEntry);
            controller.makeChangesToDatabase(editedEntry);
        }
        closeWindow(buttonPush);
    }

    private void editCommonFields(T editedEntry) throws NumberFormatException {

        if(controller.typeWasChanged()) {
            controller.editType(editedEntry);
        }
        if(controller.titleWasChanged()) {
            controller.editTitle(editedEntry);
        }
        if(controller.amountWasChanged()) {
            if(controller.amountIsValid()) {
                controller.editAmount(editedEntry);
            }
            else{
                throw new NumberFormatException();
            }
        }
    }
    private void closeWindow(ActionEvent buttonPush) {

        Stage stage = WindowUtil.getStageFromActionEvent(buttonPush);
        stage.hide();
        stage.close();
    }
}
