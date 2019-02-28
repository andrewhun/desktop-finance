/*
* This file contains the NumericInputValidationService interface. This interface
* was created to add flexibility to the processing of numeric inputs.
 */

package com.andrewhun.finance.services;

public interface NumericInputValidationService {

    Boolean inputIsCorrect(String input);
}