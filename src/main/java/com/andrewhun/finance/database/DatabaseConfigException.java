package com.andrewhun.finance.database;

/**
 * Signals a misconfiguration of {@link DatabaseConfig}, such as requesting the test
 * database storage mode without first providing a test directory.
 */
public class DatabaseConfigException extends RuntimeException {

    public DatabaseConfigException(String message) {
        super(message);
    }
}