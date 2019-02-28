/*
* This file contains the MultipleEntriesFoundException class,
* which is used to notify users when SELECT statements that
* should return a single entry had found more than one result.
 */

package com.andrewhun.finance.exceptions;

public class MultipleEntriesFoundException extends Exception {

    public MultipleEntriesFoundException() {

        super("Your query has returned multiple results when it should have returned only one");
    }
}
