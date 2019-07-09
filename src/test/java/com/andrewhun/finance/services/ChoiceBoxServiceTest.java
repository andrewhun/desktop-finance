package com.andrewhun.finance.services;

import org.junit.jupiter.api.*;
import javafx.scene.control.ChoiceBox;
import org.testfx.framework.junit5.ApplicationTest;

class ChoiceBoxServiceTest extends ApplicationTest {

    private ChoiceBoxService service = new ChoiceBoxService();
    private ChoiceBox<String> choiceBox;

    @BeforeEach void setUp() {

        choiceBox = new ChoiceBox<>();
    }

    @Test void testTypeChoiceBox() {

        testChoiceBox(EntryAttribute.TYPE);
    }

    @Test void testFrequencyChoiceBox() {

        testChoiceBox(EntryAttribute.FREQUENCY);
    }

    private void testChoiceBox(EntryAttribute attribute) {

        service.fillOutChoiceBox(choiceBox, attribute);
        assertChoicesForEntryAttribute(attribute);
    }

    private void assertChoicesForEntryAttribute(EntryAttribute attribute) {

        assertFirstChoiceIsEmptyString();
        switch (attribute) {

            case TYPE:
                assertNumberOfChoicesIs(3);
                Assertions.assertEquals(ChoiceBoxService.INCOME, choiceBox.getItems().get(1));
                Assertions.assertEquals(ChoiceBoxService.EXPENSE, choiceBox.getItems().get(2));
                break;

            case FREQUENCY:
                assertNumberOfChoicesIs(4);
                Assertions.assertEquals(ChoiceBoxService.DAILY, choiceBox.getItems().get(1));
                Assertions.assertEquals(ChoiceBoxService.WEEKLY, choiceBox.getItems().get(2));
                Assertions.assertEquals(ChoiceBoxService.MONTHLY, choiceBox.getItems().get(3));
        }

    }

    private void assertFirstChoiceIsEmptyString() {

        Assertions.assertEquals("", choiceBox.getItems().get(0));
    }

    private void assertNumberOfChoicesIs(int choices) {

        Assertions.assertEquals(choices, choiceBox.getItems().size());
    }
}