package com.andrewhun.finance.config;

import java.util.Objects;

public class AppContext {

    private static StorageMode storageMode;

    /** The backend the user mapper will build against on next use. */
    public static StorageMode storageMode() {
        return storageMode;
    }

    /**
     * Selects the active backend for this run. Called once at startup by the composition
     * root before any mapper is used; the change takes effect the next time a store is
     * built.
     */
    public static void setStorageMode(StorageMode actualStorageMode) {

        if (storageMode != null) {
            throw new AppContextException("Storage mode has already been set");
        }
        storageMode = Objects.requireNonNull(actualStorageMode, "storage mode");
    }

    /** Test-support hook: clears the selected backend so it can be set again. */
    public static void reset() {
        storageMode = null;
    }
}
