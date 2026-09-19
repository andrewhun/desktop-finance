package com.andrewhun.finance.database;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Holds the storage configuration the production application runs against: where the real
 * database lives. This is the single place that knows those details; tests build their own
 * providers instead of going through here.
 *
 * <p>The active {@link StorageMode} is owned by the {@code AppContext} composition root and
 * read lazily here when {@code AppContext} first builds its mapper.
 */
public class DatabaseConfig {

    private static final String DATABASE_FILE_NAME = "desktop_finance.db";

    private static Path testDirectory;

    public static void setTestDirectory (Path actualTestDirectory) {
        testDirectory = actualTestDirectory;
    }

    public static ConnectionProvider databaseConnectionProvider() {

        Path databaseDirectory;
        if(AppContext.storageMode() == StorageMode.TEST_DATABASE) {
            databaseDirectory = testDirectory();
        }
        else {
            databaseDirectory = productionDirectory();
        }
        return new SqliteConnectionProvider(databaseDirectory.resolve(DATABASE_FILE_NAME));
    }

    private static Path productionDirectory() {

        String appdata = System.getenv("APPDATA");
        if (appdata == null) {
            throw new IllegalStateException("APPDATA environment variable not set");
        }
        return Paths.get(appdata, "Desktop Finance");
    }

    private static Path testDirectory() {

        if (testDirectory == null) {
            String errorMessage = "Test directory must be set before using the test database storage mode";
            throw new DatabaseConfigException(errorMessage);
        }
        return testDirectory;
    }
}