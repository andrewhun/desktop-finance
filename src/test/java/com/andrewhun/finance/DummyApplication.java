package com.andrewhun.finance;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import com.andrewhun.finance.util.GuiElementIds;
import com.andrewhun.finance.util.NamedConstants;
import org.testfx.framework.junit5.ApplicationTest;

public abstract class DummyApplication extends ApplicationTest {

    @Override
    public void start (Stage primaryStage) throws Exception {

        captureStage(primaryStage);
        Parent root = getParent();
        Button testButton = (Button)root.lookup(GuiElementIds.TEST_BUTTON_ID);
        setButtonAction(testButton);
        showStage(primaryStage, root);
    }

    public void captureStage(Stage stage) {

        // Subclasses can use this method to get a reference to the Stage object used by the application.
    }

    private Parent getParent() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(NamedConstants.TEST_WINDOW_PATH));
        return loader.load();
    }

    private void showStage(Stage stage, Parent root) {

        stage.setTitle(NamedConstants.ORIGINAL_TEST_TITLE);
        stage.setScene(new Scene(root));
        stage.show();
    }

    abstract public void setButtonAction(Button button);
}
