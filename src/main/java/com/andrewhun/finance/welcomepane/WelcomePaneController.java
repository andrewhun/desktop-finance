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
import javafx.scene.Node;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import com.andrewhun.finance.util.SceneSwitcher;
import com.andrewhun.finance.services.UserService;

public class WelcomePaneController {

    @FXML private LoginTabPageController loginTabPageController;
    @FXML private RegisterTabPageController registerTabPageController;
    @FXML private Tab registerTab;

    @FXML private void initialize() {

        loginTabPageController.injectWelcomePaneController(this);
        registerTabPageController.injectWelcomePaneController(this);
        limitNumberOfUsers();
    }

    void switchToMainWindow(ActionEvent buttonPush) {

        // Get the stage object used by the application
        Stage stage = (Stage)((Node)buttonPush.getSource()).getScene().getWindow();

        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.showMainWindow();
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

        return new UserService().getNumberOfUsers();
    }
}