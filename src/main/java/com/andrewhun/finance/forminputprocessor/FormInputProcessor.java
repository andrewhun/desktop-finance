/*
This file contains the FormInputProcessor interface, which is implemented by classes that encapsulate the
logic of processing user input from forms.
 */

package com.andrewhun.finance.forminputprocessor;

import javafx.event.ActionEvent;

public interface FormInputProcessor {

    void processFormInput(ActionEvent buttonPush) throws Exception;
}
