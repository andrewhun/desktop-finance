/*
* This file contains the AmountInputValidationService class. This class is used
* when numeric inputs, which are interpreted as amounts need validation. This pretty
* much means all numeric inputs except for the starting balance value. The reason
* behind this distinction is the fact that starting balances can be zero or negative,
* but other amounts can not.
 */

package com.andrewhun.finance.services;

public class AmountInputValidationService implements NumericInputValidationService {

    public Boolean inputIsCorrect(String input) {

        try {
            Double amount = Double.parseDouble(input);
            return (amount >= 0);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}