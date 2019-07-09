package com.andrewhun.finance.services;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import com.andrewhun.finance.util.*;
import com.andrewhun.finance.DummyApplication;
import static org.junit.jupiter.api.Assertions.*;

class WindowServiceTest extends DummyApplication {

    private final String NOT_A_POPUP = "Not a popup";

    private Boolean showPopup;
    private Stage testStage;

    @BeforeEach void setUp() {

        showPopup = false;
    }

    @Override public void captureStage(Stage stage) {

        testStage = stage;
    }

     @Override public void setButtonAction(Button button) {

        button.setOnAction((ActionEvent event) -> {

                try {
                    if (showPopup) {
                        showPopupWindow();
                    }

                    else {
                        showNormalWindow(event);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        });
    }

    private void showNormalWindow(ActionEvent event) {

        Stage stage = WindowUtil.getStageFromActionEvent(event);
        WindowService windowService = new WindowService(stage);
        windowService.showSelectedWindow(NamedConstants.TEST_EDIT_FORM_PATH, NOT_A_POPUP);
    }

    private void showPopupWindow() throws Exception {

        WindowService windowService = new WindowService(new Stage());
        FXMLLoader loader = new FXMLLoader(getClass().getResource(NamedConstants.TEST_EDIT_FORM_PATH));
        Parent root = loader.load();
        windowService.showPopupWindow("Popup window", root);
    }

    @Test void testShowSelectedWindow() {

        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertTrue(testStage.isFocused());
        assertEquals(NOT_A_POPUP, testStage.getTitle());
    }

    @Test void testShowPopupWindow() {

        showPopup = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertFalse(testStage.isFocused());
        assertEquals(NamedConstants.ORIGINAL_TEST_TITLE, testStage.getTitle());
    }
}