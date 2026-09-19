package com.andrewhun.finance.config;

/**
 * Wraps a checked {@link java.sql.SQLException} raised while building or initialising
 * storage, so callers that only reach for a repository need not declare it. A failure
 * here is fatal: the application cannot run without its storage backend.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}