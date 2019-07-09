/*
This file contains the WindowService class, which is responsible for handling low-level tasks regarding
windows (showing the specified scene either in the main window or a popup window.)
 */

package com.andrewhun.finance.services;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;

public class WindowService {

    private Stage stage;

    public WindowService(Stage stage) {

        this.stage = stage;
    }

    public void showSelectedWindow(String fxmlPath, String title) {

        try {
            stage.hide();
            Parent root = getFxmlLoader(fxmlPath).load();
            setUpStage(title, root);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showPopupWindow(String title, Parent root){

        stage.initModality(Modality.APPLICATION_MODAL);
        setUpStage(title, root);
    }

    private void setUpStage(String title, Parent root) {

        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private FXMLLoader getFxmlLoader(String fxmlPath) {

        return new FXMLLoader(getClass().getResource(fxmlPath));
    }
}
