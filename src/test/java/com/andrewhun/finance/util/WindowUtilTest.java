package com.andrewhun.finance.util;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;

public class WindowUtilTest extends ApplicationTest {

    private final String BUTTON_ID = "id";
    private ActionEvent actionEvent;
    private Stage stage;

    private Application application = new Application() {

        @Override
        public void start(Stage primaryStage) {

            showWindow(primaryStage, createButton());
        }
    };

    @Override public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        application.start(primaryStage);
    }

    @Test void testGetStageFromActionEvent() {

        clickOn("#" + BUTTON_ID);
        Assertions.assertEquals(stage, WindowUtil.getStageFromActionEvent(actionEvent));
    }

    private void showWindow(Stage stage, Button button) {


        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button);
        Scene scene = new Scene(stackPane, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton() {

        Button button = new Button("Test");
        button.setId(BUTTON_ID);
        button.setOnAction((ActionEvent event) -> actionEvent = event);
        return button;
    }
}
