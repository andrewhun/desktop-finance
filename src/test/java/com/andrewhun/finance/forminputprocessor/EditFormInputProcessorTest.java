package com.andrewhun.finance.forminputprocessor;

import com.andrewhun.finance.DummyApplication;
import com.andrewhun.finance.models.ModifiableEntry;
import com.andrewhun.finance.util.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EditFormInputProcessorTest extends DummyApplication {

    private String type;
    private String title;
    private String amount;

    private Boolean typeWasChanged;
    private Boolean titleWasChanged;
    private Boolean amountWasChanged;
    private Boolean amountIsValid;

    private String errorMessage;

    private EditFormInputProcessor processor;

    @BeforeEach
    void setUp() {

        type = NamedConstants.ORIGINAL;
        title = NamedConstants.ORIGINAL;
        amount = NamedConstants.ORIGINAL;

        typeWasChanged = false;
        titleWasChanged = false;
        amountWasChanged = false;
        amountIsValid = false;

        processor = new EditFormInputProcessor<>(createEditFormController());
    }

    @Override public void setButtonAction(Button button) {

        button.setOnAction((ActionEvent event) -> {

            try {
                processor.processFormInput(event);
            }
            catch (Exception ignore) {
            }
        });
    }

    @Test
    void testNoChangesWereMade() {

        clickOn(GuiElementIds.TEST_BUTTON_ID);

        assertValues(NamedConstants.ORIGINAL, NamedConstants.ORIGINAL, NamedConstants.ORIGINAL);
        assertWindowIsClosed();
    }

    @Test void testChangingType() {

        typeWasChanged = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);

        assertValues(NamedConstants.EDITED, NamedConstants.ORIGINAL, NamedConstants.ORIGINAL);
        assertWindowIsClosed();
    }

    @Test void testChangingTitle() {

        titleWasChanged = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);

        assertValues(NamedConstants.ORIGINAL, NamedConstants.EDITED, NamedConstants.ORIGINAL);
        assertWindowIsClosed();
    }

    @Test void testNumberFormatException() {

        amountWasChanged = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);
        assertEquals(ErrorMessages.INVALID_NUMERIC_INPUT_MESSAGE, errorMessage);
    }

    @Test void testChangingAmount() {

        amountWasChanged = true;
        amountIsValid = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);

        assertValues(NamedConstants.ORIGINAL, NamedConstants.ORIGINAL, NamedConstants.EDITED);
        assertWindowIsClosed();
    }

    @Test void testChangingAll() {

        typeWasChanged = true;
        titleWasChanged = true;
        amountWasChanged = true;
        amountIsValid = true;
        clickOn(GuiElementIds.TEST_BUTTON_ID);

        assertValues(NamedConstants.EDITED, NamedConstants.EDITED, NamedConstants.EDITED);
        assertWindowIsClosed();
    }

    private void assertValues(String typeValue, String titleValue, String amountValue) {

        assertEquals(typeValue, type);
        assertEquals(titleValue, title);
        assertEquals(amountValue, amount);
    }

    private void assertWindowIsClosed() {

        assertThrows(FxRobotException.class, () -> clickOn(GuiElementIds.TEST_BUTTON_ID));
    }

    private EditFormController<ModifiableEntry> createEditFormController() {

        return new EditFormController<>() {

            @Override
            public void injectEntry(ModifiableEntry entry) {}

            @Override
            public Boolean aChangeWasMade() {
                return (typeWasChanged || titleWasChanged || amountWasChanged);
            }

            @Override
            public Boolean typeWasChanged() {
                return typeWasChanged;
            }

            @Override
            public void editType(ModifiableEntry entry) {

                type = NamedConstants.EDITED;
            }

            @Override
            public Boolean titleWasChanged() {
                return titleWasChanged;
            }

            @Override
            public void editTitle(ModifiableEntry entry) {

                title = NamedConstants.EDITED;
            }

            @Override
            public Boolean amountIsValid() {
                return amountIsValid;
            }

            @Override
            public Boolean amountWasChanged() {
                return amountWasChanged;
            }

            @Override
            public void editAmount(ModifiableEntry entry) {

                amount = NamedConstants.EDITED;
            }

            @Override
            public ModifiableEntry copyOriginalEntry() {
                return null;
            }

            @Override
            public void makeChangesToDatabase(ModifiableEntry editedEntry) {

            }

            @Override
            public void showErrorMessage(String message) {

                errorMessage = message;
            }

        };
    }
}
