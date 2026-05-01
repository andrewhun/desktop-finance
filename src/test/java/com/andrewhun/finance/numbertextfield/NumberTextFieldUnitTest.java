package com.andrewhun.finance.numbertextfield;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NumberTextFieldUnitTest {

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX toolkit (required for JavaFX component testing)
        new JFXPanel();
    }

    // ==================== Min/Max Tests ====================

    @Test
    @DisplayName("Should throw an exception at minimum value that exceeds maximum")
    void testMinValueExceedingMax() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setMaxValue(100);

        try {
            field.setMinValue(100.01);
            fail("Should throw exception for invalid min value");
        }
        catch (NumberTextFieldException e) {
            assertEquals("The minimum value cannot be greater than the maximum", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should accept valid minimum value")
    void testValidMinValue() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setMaxValue(100);

        try {
            field.setMinValue(100);
            assertEquals(100, field.getMinValue());
        }
        catch (NumberTextFieldException e) {
            fail("Should not throw exception for valid min value");
        }
    }

    @Test
    @DisplayName("Should throw an exception at maximum value that is below minimum")
    void testMaxValueBelowMin() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setMinValue(100);

        try {
            field.setMaxValue(99.99);
            fail("Should throw exception for invalid min value");
        }
        catch (NumberTextFieldException e) {
            assertEquals("The maximum value must be greater than or equal to the minimum", e.getMessage());
        }
    }

    @Test
    @DisplayName("Should accept valid maximum value")
    void testValidMaxValue() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setMinValue(100);

        try {
            field.setMaxValue(100);
            assertEquals(100, field.getMaxValue());
        }
        catch (NumberTextFieldException e) {
            fail("Should not throw exception for valid max value");
        }
    }

    // ==================== Text Value Validation Tests ====================

    @Test
    @DisplayName("Should validate standard decimal with maximum decimal places")
    void testValidatingValidDecimalWithMaxPlaces() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        assertValidTextValue(field, "123" + decimalSeparator + "45");
        assertValidTextValue(field, "0" + decimalSeparator + "99");
        assertValidTextValue(field, "1000" + decimalSeparator + "00");
    }

    @Test
    @DisplayName("Should validate large decimal with maximum decimal places")
    void testValidatingLargeValidDecimalWithMaxPlaces() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        assertValidTextValue(field, "10000" + decimalSeparator + "00");
        assertValidTextValue(field, "100000" + decimalSeparator + "00");
        assertValidTextValue(field, "1000000" + decimalSeparator + "00");
        assertValidTextValue(field, "1000000000" + decimalSeparator + "00");
        assertValidTextValue(field, "1000000000000" + decimalSeparator + "00");
    }

    @Test
    @DisplayName("Should reject decimal with more than maximum decimal places")
    void testValidatingInvalidDecimalWithTooManyPlaces() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        assertInvalidTextValue(field, "123" + decimalSeparator + "456");
        assertInvalidTextValue(field, "0" + decimalSeparator + "999");
        assertInvalidTextValue(field, "10" + decimalSeparator + "123456");
    }

    @Test
    @DisplayName("Should reject minus sign when negative values not allowed")
    void testValidatingInvalidMinusSignWhenDisabled() {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(false);
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        assertInvalidTextValue(field, "-123" + decimalSeparator + "45");
        assertInvalidTextValue(field, "-50");
        assertInvalidTextValue(field, "-0" + decimalSeparator + "01");
    }

    @Test
    @DisplayName("Should accept minus sign when negative values allowed")
    void testValidatingValidMinusSignWhenEnabled() {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(true);
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        assertValidTextValue(field, "-123" + decimalSeparator + "45");
        assertValidTextValue(field, "-50");
        assertValidTextValue(field, "-0" + decimalSeparator + "01");
    }

    @Test
    @DisplayName("Should reject multiple decimal separators")
    void testValidatingInvalidMultipleDecimals() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);
        char decimalSeparator = getDecimalSeparator();

        assertInvalidTextValue(field, "12" + decimalSeparator + "34" + decimalSeparator + "56");
        assertInvalidTextValue(field, "12" + decimalSeparator + "34" + decimalSeparator + "56");
        assertInvalidTextValue(field, "0" + decimalSeparator + "0" + decimalSeparator + "0");
    }

    @Test
    @DisplayName("Should accept empty text as valid")
    void testValidatingValidEmptyText() {
        NumberTextField field = new NumberTextField();
        assertValidTextValue(field, "");
        assertValidTextValue(field, null);
    }

    @Test
    @DisplayName("Should reject letters in text")
    void testValidatingInvalidLetters() {

        NumberTextField field = new NumberTextField();
        assertInvalidTextValue(field, "abc");
        assertInvalidTextValue(field, "123abc");
        assertInvalidTextValue(field, "12" + getDecimalSeparator() + "3a4");
    }

    @Test
    @DisplayName("Should accept integers when decimal places is 0")
    void testValidatingValidIntegerOnly() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(0);

        assertValidTextValue(field, "123");
        assertValidTextValue(field, "0");
        assertInvalidTextValue(field, "123" + getDecimalSeparator() + "45");
    }

    @Test
    @DisplayName("Should reject decimal separator when decimal places is 0")
    void testValidatingInvalidDecimalSeparator() {
        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(0);
        assertInvalidTextValue(field, "123" + getDecimalSeparator());
    }

    @Test
    @DisplayName("Should validate partial input during typing")
    void testValidatingValidPartialInput() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        // Simulating progressive typing
        assertValidTextValue(field, "1");
        assertValidTextValue(field, "12");
        assertValidTextValue(field, "123");
        assertValidTextValue(field, "123" + decimalSeparator);
        assertValidTextValue(field, "123" + decimalSeparator + "4");
        assertValidTextValue(field, "123" + decimalSeparator + "45");
        // Too many decimals
        assertInvalidTextValue(field, "123" + decimalSeparator + "456");
    }

    @Test
    @DisplayName("Should validate negative incomplete decimal")
    void testValidatingValidIncompleteNegative() {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(true);
        field.setDecimalPlaces(2);

        char decimalSeparator = getDecimalSeparator();

        // Simulating progressive typing
        assertValidTextValue(field, "-");
        assertValidTextValue(field, "-1");
        assertValidTextValue(field, "-12");
        assertValidTextValue(field, "-12" + decimalSeparator);
        assertValidTextValue(field, "-12" + decimalSeparator + "3");
        assertValidTextValue(field, "-12" + decimalSeparator + "34");
        // Too many decimals
        assertInvalidTextValue(field, "-12" + decimalSeparator + "345");
    }

    @Test
    @DisplayName("Should reject minus sign followed by decimal")
    void testValidatingInvalidMinusSignFollowedByDecimal() {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(true);
        field.setDecimalPlaces(2);

        assertInvalidTextValue(field, "-" + getDecimalSeparator());
    }

    @Test
    @DisplayName("Should reject multiple minus signs")
    void testValidatingInvalidMultipleMinusSigns() {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(true);

        assertInvalidTextValue(field, "--123");
        assertInvalidTextValue(field, "-12-3");
        assertInvalidTextValue(field, "123--");
    }

    @Test
    @DisplayName("Should validate based on current configuration")
    void testValidatingDynamicConfiguration() {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);
        field.setAllowNegative(false);

        char decimalSeparator = getDecimalSeparator();

        assertValidTextValue(field, "123" + decimalSeparator + "45");
        assertInvalidTextValue(field, "-123" + decimalSeparator + "45");
        assertInvalidTextValue(field, "123" + decimalSeparator + "456");

        field.setAllowNegative(true);
        field.setDecimalPlaces(3);

        assertValidTextValue(field, "123" + decimalSeparator + "45");
        assertValidTextValue(field, "-123" + decimalSeparator + "45");
        assertValidTextValue(field, "123" + decimalSeparator + "456");
    }

    private void assertValidTextValue(NumberTextField field, String textValue) {
        field.setText("");
        field.setText(textValue);
        assertEquals(textValue, field.getText());
    }

    private void assertInvalidTextValue(NumberTextField field, String textValue) {
        field.setText("");
        field.setText(textValue);
        assertTrue(field.getText().isBlank());
    }

    // ==================== Parse Input Tests ====================

    @Test
    @DisplayName("Should parse standard decimal with maximum decimal places")
    void testParsingValidDecimalWithMaxPlaces() {
        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);
        verifyParsingValidInput(field, "123" + getDecimalSeparator() + "45", BigDecimal.valueOf(123.45));
    }

    /*
     * The Number Format parser can handle incomplete inputs that end with the decimal separator.
     * The text input validation will prevent the user from entering the separator when decimals are not allowed,
     * so this should not cause an issue. We can incorporate this rule into the behaviour of our field
     */
    @Test
    @DisplayName("Should parse incomplete decimal")
    void testParsingValidIncompleteDecimal() {
        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(1);
        verifyParsingValidInput(field, "123" + getDecimalSeparator(), BigDecimal.valueOf(123));
    }

    @Test
    @DisplayName("Should parse empty text as zero when field value is not required")
    void testParsingValidEmptyText() {
        NumberTextField field = new NumberTextField();
        field.setRequired(false);
        verifyParsingValidInput(field, "", BigDecimal.valueOf(0));
    }

    private void verifyParsingValidInput(NumberTextField field, String input, BigDecimal expectedValue) {

        field.setText(input);
        try {
            field.parseInput();
            assertEquals(expectedValue, field.numberProperty().getValue());
        }
        catch (NumberTextFieldException e) {
            fail("Should not throw exception for valid input");
        }
    }

    @Test
    @DisplayName("Should throw an exception at blank value when field value is required")
    void testParsingInvalidEmptyText() {

        NumberTextField field = new NumberTextField();
        field.setRequired(true);

        String expectedErrorMessage = "Please enter a value";
        verifyParsingInvalidInput(field, "", expectedErrorMessage);
    }

    @Test
    @DisplayName("Should throw an exception at value below minimum")
    void testParsingInvalidBelowMinimumValue() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setAllowNegative(true);
        field.setDecimalPlaces(2);
        field.setMinValue(-1);

        String expectedErrorMessage = "The value can't be lower than " + field.getMinValue();
        verifyParsingInvalidInput(field, "-1" + getDecimalSeparator() + "01", expectedErrorMessage);
    }

    @Test
    @DisplayName("Should throw an exception at value above maximum")
    void testParsingInvalidAboveMaximumValue() throws NumberTextFieldException {

        NumberTextField field = new NumberTextField();
        field.setDecimalPlaces(2);
        field.setMaxValue(1);

        String expectedErrorMessage = "The value can't be greater than " + field.getMaxValue();
        verifyParsingInvalidInput(field, "1" + getDecimalSeparator() + "01", expectedErrorMessage);
    }

    private void verifyParsingInvalidInput(NumberTextField field, String input, String expectedErrorMessage) {

        field.setText(input);
        try {
            field.parseInput();
            fail("Should throw exception for invalid input");
        }
        catch (NumberTextFieldException e) {
            String message = e.getMessage();
            assertEquals(message, expectedErrorMessage);
        }
    }

    private char getDecimalSeparator() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        return symbols.getDecimalSeparator();
    }
}