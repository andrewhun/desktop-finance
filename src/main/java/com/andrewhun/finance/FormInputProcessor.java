/*
* This file contains the FormInputProcessor class, which encapsulates
* the general logic behind form input validation and processing.
 */

package com.andrewhun.finance;

import javafx.event.ActionEvent;
import static com.andrewhun.finance.util.ErrorMessages.*;

public abstract class FormInputProcessor {

    public void processFormInput(ActionEvent buttonPush) {

        try {
            if (inputIsValid()) {
                makeChangesToDatabase(buttonPush);
            } else {
                reactToInvalidInput();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            showErrorMessage(CONTACT_DEVELOPERS_MESSAGE);
        }
    }

    private Boolean inputIsValid() throws Exception {

        return (!aRequiredFieldIsEmpty() && inputIsCorrect());
    }

    private void reactToInvalidInput() throws Exception {

        clearErrorMessageDisplay();

        if (aRequiredFieldIsEmpty()) {
            showErrorMessage(REQUIRED_FIELD_EMPTY_MESSAGE);
        } else {
            reactToIncorrectInput();
            if (errorMessageDisplayIsEmpty()) {
                showErrorMessage(SOMETHING_HAS_GONE_WRONG_MESSAGE);
            }
        }
    }

    protected abstract Boolean inputIsCorrect() throws Exception;
    protected abstract void makeChangesToDatabase(ActionEvent buttonPush) throws Exception;
    protected abstract void reactToIncorrectInput() throws Exception;
    protected abstract Boolean aRequiredFieldIsEmpty();
    protected abstract void showErrorMessage(String message);
    protected abstract void clearErrorMessageDisplay();
    protected abstract Boolean errorMessageDisplayIsEmpty();
}