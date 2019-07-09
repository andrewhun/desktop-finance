/*
This file contains the WindowUtil class, which performs low-level tasks related to windows. These tasks
do not require the instantiation of the object (i. e. they are static), unlike in the case of WindowService.
 */

package com.andrewhun.finance.util;

import java.net.URL;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

public class WindowUtil {

    private WindowUtil() {}

    public static Stage getStageFromActionEvent(ActionEvent actionEvent) {

        return (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
    }

    public static void reloadPage(Stage stage, URL url) throws Exception {

        Scene scene = stage.getScene();
        scene.setRoot(FXMLLoader.load(url));
    }
}
