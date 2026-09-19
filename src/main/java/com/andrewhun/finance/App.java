package com.andrewhun.finance;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;
import com.andrewhun.finance.database.DatabaseInitializer;
import com.andrewhun.finance.services.Page;
import com.andrewhun.finance.services.WindowNavigator;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.usermapper.UserMapperFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Optional;

public class App extends Application {

    private final StorageMode storageMode;

    /** Used by the JavaFX runtime via {@link #launch}; runs against the production wiring. */
    public App() {
        this(StorageMode.PRODUCTION_DATABASE);
    }

    /** Lets a caller (e.g. a test) supply an alternative wiring. */
    public App(StorageMode storageMode) {
        this.storageMode = storageMode;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext.setStorageMode(storageMode);
        DatabaseInitializer.initialize();
        showFirstWindow(primaryStage);
    }

    private void showFirstWindow(Stage primaryStage) throws Exception {

        if (aUserIsLoggedIn()) {
            WindowNavigator.showWindow(primaryStage, Page.MAIN_WINDOW);
        }
        else {
            WindowNavigator.showWindow(primaryStage, Page.LOGIN);
        }
    }

    private Boolean aUserIsLoggedIn() {
        Optional<User> user = UserMapperFactory.create().findLoggedIn();
        return user.isPresent();
    }
}