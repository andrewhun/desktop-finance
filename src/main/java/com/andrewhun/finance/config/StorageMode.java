package com.andrewhun.finance.config;

/**
 * Selects which backend the user repository runs against: a real SQLite database, or an
 * in-memory store. Resolved by {@link AppContext#storageMode()}.
 */
public enum StorageMode {
    PRODUCTION_DATABASE,
    TEST_DATABASE,
    IN_MEMORY
}