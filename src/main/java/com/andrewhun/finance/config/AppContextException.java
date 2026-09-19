package com.andrewhun.finance.config;

/**
 * Signals a misuse of {@link AppContext}'s startup wiring, such as attempting to select
 * the storage backend more than once.
 */
public class AppContextException extends RuntimeException {

    public AppContextException(String message) {
        super(message);
    }
}