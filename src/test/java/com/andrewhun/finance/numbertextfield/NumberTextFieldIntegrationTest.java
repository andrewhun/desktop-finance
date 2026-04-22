package com.andrewhun.finance.numbertextfield;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.*;

public class NumberTextFieldIntegrationTest extends ApplicationTest {

    private NumberTextField field;
    private NumberTextField otherField; // For focus testing

    @BeforeAll
    static void setUpHeadlessMode() {

        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void start(Stage stage) {
        field = new NumberTextField();
        field.setId("field");

        otherField = new NumberTextField();
        otherField.setId("otherField");

        VBox root = new VBox(10, field, otherField);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    // ==================== Typing Behaviour Tests ====================

    // The filtering behaviour is tested in depth via the unit test class, so including a single test case here should
    // be sufficient.

    @Test
    @DisplayName("Should only accept valid input from the user")
    void testFilteringUserInput() {

        field.setDecimalPlaces(2);
        clickOn("#field");
        String validText = "123" + getDecimalSeparator() + "01";
        String fullText = "abc" + validText + "@;#";
        write(fullText);
        assertEquals(validText, field.getText());
    }

    // ==================== Focus Behaviour Tests ====================

    @Test
    @DisplayName("Should parse a valid positive number without any errors")
    void testParsingValidPositiveNumber() {
        field.setDecimalPlaces(2);
        verifyParsingValidValue("123" + getDecimalSeparator() + "45", BigDecimal.valueOf(123.45));
    }

    @Test
    @DisplayName("Should parse a valid negative number without any errors")
    void testParsingValidNegativeNumber() {
        field.setDecimalPlaces(2);
        field.setAllowNegative(true);
        verifyParsingValidValue("-123" + getDecimalSeparator() + "45", BigDecimal.valueOf(-123.45));
    }

    @Test
    @DisplayName("Should parse a blank value without any errors if input is not required")
    void testParsingValidBlankValue() {
        verifyParsingValidValue("", BigDecimal.valueOf(0));
    }

    @Test
    @DisplayName("Should parse an incomplete decimal without any errors")
    void testParsingValidIncompleteDecimal() {
        field.setDecimalPlaces(2);
        verifyParsingValidValue("123" + getDecimalSeparator(), BigDecimal.valueOf(123));
    }

    private void verifyParsingValidValue(String input, BigDecimal expectedValue) {

        clickOn("#field");
        write(input);
        clickOn("#otherField");

        assertEquals(expectedValue, field.numberProperty().getValue());
        assertNoError();
    }

    private void assertNoError() {
        assertFalse(field.hasError());
        assertTrue(field.getErrorMessage().isBlank());
        assertFalse(field.getNumberFieldStyleClass().contains("error"));
    }

    @Test
    @DisplayName("Should show an error when required input is blank")
    void testParsingMissingRequiredInput() {
        field.setRequired(true);
        verifyParsingInvalidValue("","Please enter a value");
    }

    @Test
    @DisplayName("Should show an error when the input is lower than the minimum")
    void testParsingValueBelowMinimum() throws NumberTextFieldException {

        field.setDecimalPlaces(2);
        field.setMinValue(1);
        verifyParsingInvalidValue("0" + getDecimalSeparator() + "99",
                "The value can't be lower than " + field.getMinValue());
    }

    @Test
    @DisplayName("Should show an error when the input is greater than the maximum")
    void testParsingValueAboveMaximum() throws NumberTextFieldException {

        field.setDecimalPlaces(2);
        field.setMaxValue(1);
        verifyParsingInvalidValue("1" + getDecimalSeparator() + "01",
                "The value can't be greater than " + field.getMaxValue());
    }

    private void verifyParsingInvalidValue(String input, String expectedErrorMessage) {

        clickOn("#field");
        write(input);
        clickOn("#otherField");

        assertEquals(BigDecimal.valueOf(0), field.numberProperty().getValue());
        assertError(expectedErrorMessage);
    }

    private void assertError(String expectedErrorMessage) {
        assertTrue(field.hasError());
        assertEquals(expectedErrorMessage, field.getErrorMessage());
        assertTrue(field.getNumberFieldStyleClass().contains("error"));
    }

    private char getDecimalSeparator() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        return symbols.getDecimalSeparator();
    }
}
