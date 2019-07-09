/*
* This file contains the NumericInputProcessor class, which is responsible for
* validating and processing numeric inputs. The validation policy partly depends
* on what type of NumericInputValidationService is used to instantiate the class.
 */

package com.andrewhun.finance.services;

import static com.andrewhun.finance.util.NamedConstants.*;

public class NumericInputProcessor {

    private NumericInputValidationService validationService;

    public NumericInputProcessor(NumericInputValidationService validationService) {

        this.validationService = validationService;
    }

    public Boolean inputIsValid(String input) {

        return (validationService.inputIsCorrect(input) && !inputIsConstantFieldValue(input));
    }

    private Boolean inputIsConstantFieldValue(String input) {

        return (input.equals(NAN) ||
                input.equals(MAX_EXPONENT) ||
                input.equals(MAX_VALUE) ||
                input.equals(MIN_EXPONENT) ||
                input.equals(MIN_NORMAL) ||
                input.equals(MIN_VALUE) ||
                input.equals(NEGATIVE_INFINITY) ||
                input.equals(POSITIVE_INFINITY));
    }

    public Double castToDouble(String input) throws NumberFormatException {

        return Double.parseDouble(input);
    }
}