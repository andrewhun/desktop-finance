/*
This file contains the FormController interface, which is implemented by classes that control
simple forms. These forms allow user to carry out operations such as the creation of new records
and logging in, but they do not let them edit existing records.
 */

package com.andrewhun.finance.forminputprocessor;

import javafx.stage.Stage;
import javafx.event.ActionEvent;

public interface FormController {

    Boolean inputIsCorrect() throws Exception;
    void makeChangesToDatabase(ActionEvent buttonPush) throws Exception;
    void reactToIncorrectInput() throws Exception;
    Boolean aRequiredFieldIsEmpty();
    void showErrorMessage(String message);
    void clearErrorMessageDisplay();
    Boolean errorMessageDisplayIsEmpty();

    // To be changed once ChangeReflectionStrategy is in place
    void reflectChangesOnUserInterface(Stage stage) throws Exception;
}
