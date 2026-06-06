package com.andrewhun.finance.database;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Path databaseDirectory = null;

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + getEffectiveDirectory().resolve("desktop_finance.db");
        return DriverManager.getConnection(url);
    }

    public static void setDatabaseDirectory(Path dir) {
        databaseDirectory = dir;
    }

    static Path getDatabaseDirectory() {
        return getEffectiveDirectory();
    }

    private static Path getEffectiveDirectory() {
        if (databaseDirectory != null) {
            return databaseDirectory;
        }
        String appdata = System.getenv("APPDATA");
        if (appdata == null) {
            throw new IllegalStateException("APPDATA environment variable not set");
        }
        return Paths.get(appdata, "Desktop Finance");
    }
}