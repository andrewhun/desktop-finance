package com.andrewhun.finance.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowNavigator {

    private WindowNavigator() {
    }

    public static void showWindow(Stage stage, Page page) throws IOException {

        Parent root = FXMLLoader.load(WindowNavigator.class.getResource(page.getFxmlPath()));
        stage.setTitle(page.getTitle());
        adjustWindowContent(stage, root);
        stage.show();
    }

    private static void adjustWindowContent(Stage stage, Parent root) {

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = createNewScene(root);
            stage.setScene(scene);
        }
        else {
            scene.setRoot(root);
        }
    }

    private static Scene createNewScene(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(WindowNavigator.class.getResource("/css/app.css").toExternalForm());
        return scene;
    }
}