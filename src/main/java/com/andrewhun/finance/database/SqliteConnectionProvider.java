package com.andrewhun.finance.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnectionProvider implements ConnectionProvider {

    private final Path databaseFile;

    public SqliteConnectionProvider(Path databaseFile) {
        this.databaseFile = databaseFile;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
    }

    @Override
    public void ensureStorageExists() throws SQLException {
        try {
            Files.createDirectories(databaseFile.getParent());
        }
        catch (IOException e) {
            throw new SQLException("Could not create database directory: " + e.getMessage(), e);
        }
    }
}