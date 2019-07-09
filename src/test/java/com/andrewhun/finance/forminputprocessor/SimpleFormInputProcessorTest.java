package com.andrewhun.finance.forminputprocessor;

import com.andrewhun.finance.DummyApplication;
import com.andrewhun.finance.forminputprocessor.FormController;
import com.andrewhun.finance.forminputprocessor.SimpleFormInputProcessor;
import com.andrewhun.finance.util.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleFormInputProcessorTest extends DummyApplication {

    private SimpleFormInputProcessor processor;

    private Boolean inputIsCorrect;
    private Boolean aRequiredFieldIsEmpty;
    private Boolean errorMessageDisplayIsEmpty;
    private Boolean throwException;

    private String errorMessage;
    private String database;

    @BeforeEach
    void setUp() {

        inputIsCorrect = false;
        aRequiredFieldIsEmpty = false;
        errorMessageDisplayIsEmpty = false;
        throwException = false;
        database = NamedConstants.ORIGINAL;
        processor = new SimpleFormInputProcessor(createFormController());
    }

    @Override public void setButtonAction(Button button) {

        button.setOnAction((ActionEvent event) -> processor.processFormInput(event));
    }

    @Test
    void testIncorrectInputMessage() {

        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertErrorMessage(NamedConstants.INCORRECT_INPUT);
    }

    @Test void testContactDevelopersMessage() {

        throwException = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertErrorMessage(ErrorMessages.CONTACT_DEVELOPERS_MESSAGE);
    }

    @Test void testSomethingHasGoneWrongMessage() {

        errorMessageDisplayIsEmpty = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertErrorMessage(ErrorMessages.SOMETHING_HAS_GONE_WRONG_MESSAGE);
    }

    @Test void testARequiredFieldIsEmptyMessage() {

        aRequiredFieldIsEmpty = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertErrorMessage(ErrorMessages.REQUIRED_FIELD_EMPTY_MESSAGE);
    }

    @Test void testMakeChangesToDatabase() {

        inputIsCorrect = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertEquals(NamedConstants.EDITED, database);
    }

    private void assertErrorMessage(String message) {

        assertEquals(message, errorMessage);
    }

    private FormController createFormController() {

        return new FormController() {

            @Override
            public Boolean inputIsCorrect() {

                return inputIsCorrect;
            }

            @Override
            public void makeChangesToDatabase(ActionEvent buttonPush) {

                database = NamedConstants.EDITED;
            }

            @Override
            public void reactToIncorrectInput() throws Exception {

                if (throwException) {

                    throw new Exception();
                }
                else {
                    errorMessage = NamedConstants.INCORRECT_INPUT;
                }
            }

            @Override
            public Boolean aRequiredFieldIsEmpty() {
                return aRequiredFieldIsEmpty;
            }

            @Override
            public void showErrorMessage(String message) {

                errorMessage = message;
            }

            @Override
            public void clearErrorMessageDisplay() {

            }

            @Override
            public Boolean errorMessageDisplayIsEmpty() {

                return errorMessageDisplayIsEmpty;
            }

            @Override
            public void reflectChangesOnUserInterface(Stage stage) {

            }
        };
    }
}
