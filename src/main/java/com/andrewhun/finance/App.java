/*
* This file contains the App class, which is responsible for starting up the application
* with the appropriate window shown to the user.
 */
package com.andrewhun.finance;

import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.application.Application;
import com.andrewhun.finance.util.SceneSwitcher;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneSwitcher sceneSwitcher = new SceneSwitcher(primaryStage);
        if (aUserIsAlreadyLoggedIn()) {
            sceneSwitcher.showMainWindow();
        }
        else {
            sceneSwitcher.showWelcomePane();
        }
    }

    private Boolean aUserIsAlreadyLoggedIn() throws SQLException{

        UserTableProcedures userTableProcedures = new UserTableProcedures();
        return userTableProcedures.aUserIsLoggedIn();
    }

    public static void main(String[] args){ Application.launch(args); }
}