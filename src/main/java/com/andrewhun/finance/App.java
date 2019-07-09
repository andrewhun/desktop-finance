/*
* This file contains the App class, which is responsible for starting up the application
* with the appropriate window shown to the user.
 */

package com.andrewhun.finance;

import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.application.Application;
import com.andrewhun.finance.services.WindowService;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        WindowService windowService = new WindowService(primaryStage);
        if (aUserIsAlreadyLoggedIn()) {
            windowService.showSelectedWindow(MAIN_WINDOW_PATH, MAIN_WINDOW_TITLE);
        }
        else {
            windowService.showSelectedWindow(WELCOME_PANE_PATH, WELCOME_PANE_TITLE);
        }
    }

    private Boolean aUserIsAlreadyLoggedIn() throws SQLException{

        UserTableProcedures userTableProcedures = new UserTableProcedures();
        return userTableProcedures.aUserIsLoggedIn();
    }

    public static void main(String[] args){ Application.launch(args); }
}