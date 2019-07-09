package com.andrewhun.finance.services;

import org.junit.jupiter.api.*;
import static com.andrewhun.finance.util.NamedConstants.*;

class NumericInputProcessorTest {

    private NumericInputProcessor amountInputProcessor =
            new NumericInputProcessor(new AmountInputValidationService());

    private NumericInputProcessor balanceInputProcessor =
            new NumericInputProcessor(new BalanceInputValidationService());

    @Test
    void testNonNumericInput() {

        Assertions.assertFalse(amountInputProcessor.inputIsValid(INCORRECT_INPUT));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(INCORRECT_INPUT));
    }

    @Test
    void testConstantFieldValues() {

        Assertions.assertFalse(amountInputProcessor.inputIsValid(NAN));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(NAN));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MAX_EXPONENT));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(MAX_EXPONENT));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MAX_VALUE));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(MAX_VALUE));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MIN_EXPONENT));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(MIN_EXPONENT));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MIN_NORMAL));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(MIN_NORMAL));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MIN_VALUE));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(MIN_VALUE));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(NEGATIVE_INFINITY));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(NEGATIVE_INFINITY));

        Assertions.assertFalse(amountInputProcessor.inputIsValid(POSITIVE_INFINITY));
        Assertions.assertFalse(balanceInputProcessor.inputIsValid(POSITIVE_INFINITY));
    }

    @Test
    void testNegativeValue() {

        final String MINUS_FIVE = "-5";

        Assertions.assertFalse(amountInputProcessor.inputIsValid(MINUS_FIVE));

        Assertions.assertTrue(balanceInputProcessor.inputIsValid(MINUS_FIVE));
    }
}