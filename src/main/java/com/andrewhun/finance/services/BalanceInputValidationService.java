/*
* This file contains the BalanceInputValidationService class, which
* is used to validate inputs that aim to manipulate the starting balance
* of a user. This implementation of NumericInputValidationService accepts
* inputs that are zero or less. This is because of my arbitrary decision
* to allow zero or negative starting balances.
 */

package com.andrewhun.finance.services;

public class BalanceInputValidationService implements NumericInputValidationService {

    public Boolean inputIsCorrect(String input) {

        try {
            Double.parseDouble(input);
            return true;
        }
        catch (NumberFormatException e) {

            return false;
        }
    }
}