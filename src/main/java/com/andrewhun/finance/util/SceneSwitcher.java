/*
* This file contains the SceneSwitcher class, which is used by the application to manage
* the changing of scenes. I decided to create a separate class because I felt that the App
* class was getting overloaded with functionality and responsibilities.
 */

package com.andrewhun.finance.util;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class SceneSwitcher {

    private Stage stage;

    public SceneSwitcher(Stage stage) {

        this.stage = stage;
    }

    public void showMainWindow() {

        String title = "Desktop Finance";
        String fxmlPath = "/fxml/MainWindow.fxml";
        showSelectedWindow(fxmlPath, title);
    }

    public void showWelcomePane() {

        String title = "Welcome to Desktop Finance!";
        String fxmlPath = "/fxml/WelcomePane.fxml";
        showSelectedWindow(fxmlPath, title);
    }

    private void showSelectedWindow(String fxmlPath, String title) {

        try {
            stage.hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
