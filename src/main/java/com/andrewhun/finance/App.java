package com.andrewhun.finance;

import com.andrewhun.finance.database.DatabaseInitializer;
import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import com.andrewhun.finance.user.UserMapper;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new DatabaseInitializer().initialize();
        UserMapper userMapper = new UserMapper();

        if (userMapper.findLoggedIn().isPresent()) {
            WindowNavigator.showWindow(primaryStage, Page.MAIN_WINDOW);
        }
        else {
            WindowNavigator.showWindow(primaryStage, Page.LOGIN);
        }
    }
}