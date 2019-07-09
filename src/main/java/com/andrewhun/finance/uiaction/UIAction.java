/*
This file contains the UIAction interface, which is implemented by classes that alter the user interface
based on the user's actions.
 */

package com.andrewhun.finance.uiaction;

import javafx.stage.Stage;

public interface UIAction {

    void performAction(Stage stage) throws Exception;
}
