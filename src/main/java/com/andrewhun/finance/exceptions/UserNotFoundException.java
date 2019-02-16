/*
* This file contains the UserNotFoundException class, which is used by a few routines of
* the UserTableProcedures class. It is supposed to indicate that the the Users table does
* not have the User the program is trying to find
 */

package com.andrewhun.finance.exceptions;

public class UserNotFoundException extends Exception {


    public UserNotFoundException(String message) {

        super(message);
    }
}