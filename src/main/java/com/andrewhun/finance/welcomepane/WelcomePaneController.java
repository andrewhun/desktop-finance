/*
* This file contains the WelcomePaneController class, which is responsible for determining
* how users can interact with the welcome pane. The class has to enforce one restriction
* only: it has to limit maximum number of profiles that can be created. Other than this,
* the class initiates the change between the welcome pane and the main window when users
* successfully login or create new profiles.
* The class having the latter task means that the Single Responsibility Principle is abused.
* I chose this implementation to avoid needlessly duplicating the contents of the
* switchToMainWindow routine.
 */

package com.andrewhun.finance.welcomepane;

import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.scene.control.Tab;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class WelcomePaneController {

    @FXML private Tab registerTab;

    @FXML private void initialize() {

        limitNumberOfUsers();
    }

    private void limitNumberOfUsers() {

        try {
            if (limitWasReached()) {
                disableUserCreation();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Boolean limitWasReached() throws SQLException {

        final int MAXIMUM_NUMBER_OF_USERS = 5;
        return(getNumberOfUsers() >= MAXIMUM_NUMBER_OF_USERS);
    }

    private void disableUserCreation() {

        registerTab.setDisable(true);
    }

    private int getNumberOfUsers() throws SQLException {

        return new UserTableProcedures().getListOfUsernames().size();
    }
}