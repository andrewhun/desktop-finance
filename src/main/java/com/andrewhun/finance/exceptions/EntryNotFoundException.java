/*
* This file contains the EntryNotFoundException class,
* which is used to notify users when their SELECT queries
* returned no results.
 */

package com.andrewhun.finance.exceptions;

public class EntryNotFoundException extends Exception {

    public EntryNotFoundException() {

        super("Could not find selected entry in database");
    }
}
