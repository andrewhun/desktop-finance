package com.andrewhun.finance.numbertextfield;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.UnaryOperator;

public class NumberTextField extends VBox {

    @FXML private Label fieldLabel;
    @FXML private TextField numberField;
    @FXML private Label errorLabel;

    private final NumberFormat nf;
    private ObjectProperty<BigDecimal> number = new SimpleObjectProperty<>();
    private final BooleanProperty allowNegative = new SimpleBooleanProperty(false);
    private final BooleanProperty required = new SimpleBooleanProperty(false);
    private final DoubleProperty minValue = new SimpleDoubleProperty(Double.NEGATIVE_INFINITY);
    private final DoubleProperty maxValue = new SimpleDoubleProperty(Double.POSITIVE_INFINITY);

    public NumberTextField() {
        this(BigDecimal.ZERO);
    }

    public NumberTextField(BigDecimal value) {
        this(value, NumberFormat.getInstance());
    }

    public NumberTextField(BigDecimal value, NumberFormat nf) {

        loadFXML();
        this.nf = nf;
        setNumber(value);
        setUpTextFormatter();
        setupParsing();
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NumberTextField.class.getResource("/fxml/NumberTextField.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load NumberTextField FXML", e);
        }
    }

    public boolean hasError() {
        if(errorLabel == null) {
            return true;
        }
        return errorLabel.isVisible();
    }

    String getErrorMessage() {
        if(errorLabel == null) {
            return null;
        }
        return errorLabel.getText();
    }

    ObservableList<String> getNumberFieldStyleClass() {
        if(numberField == null) {
            return null;
        }
        return numberField.getStyleClass();
    }

    public final void setNumber(BigDecimal value) {

        if(value.doubleValue() < 0 && !negativeValuesAllowed()) {
            return;
        }
        number.set(value);
    }

    void setText(String text) {
        numberField.setText(text);
    }

    String getText() {return numberField.getText();}

    public ObjectProperty<BigDecimal> numberProperty() {
        return number;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        nf.setMinimumFractionDigits(decimalPlaces);
        nf.setMaximumFractionDigits(decimalPlaces);
    }

    public Integer getDecimalPlaces() {
        return nf.getMinimumFractionDigits();
    }

    public boolean negativeValuesAllowed() {
        return allowNegative.get();
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative.set(allowNegative);
    }

    public boolean isRequired() {
        return required.get();
    }

    public void setRequired(boolean required) {
        this.required.set(required);
    }

    public double getMinValue() {
        return minValue.get();
    }

    public void setMinValue(double minValue) throws NumberTextFieldException {
        if(minValue > this.maxValue.get()) {
            throw new NumberTextFieldException("The minimum value cannot be greater than the maximum");
        }
        this.minValue.set(minValue);
    }

    public double getMaxValue() {
        return maxValue.get();
    }

    public void setMaxValue(double maxValue) throws NumberTextFieldException {
        if(maxValue < this.minValue.get()) {
            throw new NumberTextFieldException("The maximum value must be greater than or equal to the minimum");
        }
        this.maxValue.set(maxValue);
    }

    private void setUpTextFormatter() {
        UnaryOperator<TextFormatter.Change> filter = createTextChangeFilter();
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        numberField.setTextFormatter(textFormatter);
    }

    private UnaryOperator<TextFormatter.Change> createTextChangeFilter() {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (isValidText(newText)) {
                return change;
            }
            return null; // Reject the change
        };
        return filter;
    }

    Boolean isValidText(String textValue) {

        if(textValue == null || textValue.isBlank()) {
            return true;
        }
        return textValue.matches(getRegexPattern());
    }

    String getRegexPattern() {

        String minusSign = negativeValuesAllowed() ? "-?" : "";
        char separator = getDecimalSeparator();

        String decimals = getDecimalPlaces() > 0 ? "(\\" + separator + "\\d{0," + getDecimalPlaces() + "})?" : "";
        // This pattern can handle "standard" decimals and European number formats (values above and below 1000)
        //return "^" + minusSign + "((\\d{0,3})|((\\d{1,3}){1}(\\h\\d{3})+))?" + decimals + "$";
        return "^(" + minusSign + ")|(" + minusSign + "\\d+" + decimals + ")$";
    }

    private char getDecimalSeparator() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        return symbols.getDecimalSeparator();
    }

    private void setupParsing() {
        if (numberField != null) {
            numberField.focusedProperty().addListener(createNumberFieldFocusListener());
        }
    }

    private ChangeListener<Boolean> createNumberFieldFocusListener() {
        ChangeListener<Boolean> listener = (obs, wasFocused, isNowFocused) -> {
            clearError();
            if (!isNowFocused) {
                try {
                    parseInput();
                }
                catch (NumberTextFieldException e) {
                    showError(e.getMessage());
                }
            }
        };
        return listener;
    }

    /**
     * Tries to parse the user input to a number according to the provided
     * NumberFormat
     */
    void parseInput() throws NumberTextFieldException {

        String input = numberField.getText();
        if(inputIsMissing(input)) {
            if(!isRequired()) {
                return;
            }
            throw new NumberTextFieldException("Please enter a value");
        }
        Number parsedNumber = parseEnteredNumber(input);
        verifyParsedNumber(parsedNumber);
        setNumber(new BigDecimal(parsedNumber.toString()));
    }

    private boolean inputIsMissing(String input) {
        return (input == null || input.isBlank());
    }

    private Number parseEnteredNumber(String input) throws NumberTextFieldException {

        try {
            return nf.parse(input);
        }
        catch (ParseException e) {
            throw new NumberTextFieldException(e.getMessage());
        }
    }

    private void verifyParsedNumber(Number parsedNumber) throws NumberTextFieldException {
        if(parsedNumber.doubleValue() < getMinValue()) {
            throw new NumberTextFieldException("The value can't be lower than " + getMinValue());
        }

        if(parsedNumber.doubleValue() > getMaxValue()) {
            throw new NumberTextFieldException("The value can't be greater than " + getMaxValue());
        }
    }

    private void showError(String message) {

        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            numberField.getStyleClass().add("error");
        }
    }

    private void clearError() {

        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            numberField.getStyleClass().remove("error");
        }
    }
}