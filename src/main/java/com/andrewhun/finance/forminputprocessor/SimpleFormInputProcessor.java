/*
 * This file contains the SimpleFormInputProcessor class, which encapsulates
 * the general logic behind form input validation and processing. This variant is useful
 * for basic input forms (i. e. forms which do not allow users to edit existing database records).
 */

package com.andrewhun.finance.forminputprocessor;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.andrewhun.finance.util.WindowUtil;

import static com.andrewhun.finance.util.ErrorMessages.*;

public class SimpleFormInputProcessor implements FormInputProcessor {

    private FormController controller;

    public SimpleFormInputProcessor(FormController controller) {

        this.controller = controller;
    }

    public void processFormInput(ActionEvent buttonPush) {

        try {
            if (inputIsValid()) {
                controller.makeChangesToDatabase(buttonPush);
                Stage stage = WindowUtil.getStageFromActionEvent(buttonPush);
                controller.reflectChangesOnUserInterface(stage);

            } else {
                reactToInvalidInput();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            controller.showErrorMessage(CONTACT_DEVELOPERS_MESSAGE);
        }
    }

    private Boolean inputIsValid() throws Exception {

        return (! controller.aRequiredFieldIsEmpty() &&  controller.inputIsCorrect());
    }

    private void reactToInvalidInput() throws Exception {

        controller.clearErrorMessageDisplay();

        if ( controller.aRequiredFieldIsEmpty()) {
            controller.showErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
        }
        else {
            controller.reactToIncorrectInput();
            if ( controller.errorMessageDisplayIsEmpty()) {
                controller.showErrorMessage(SOMETHING_HAS_GONE_WRONG_MESSAGE);
            }
        }
    }
}