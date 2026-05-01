/*
* This file contains the App class, which is responsible for starting up the application
* with the appropriate window shown to the user.
 */
package com.andrewhun.finance;

/**
 * Hello world!
 *
 */
import com.andrewhun.finance.numbertextfield.NumberTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        NumberTextField field = new NumberTextField();
        Button btn = new Button();
        btn.setText("Validate Number");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println(field.numberProperty());
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(field);
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}