package com.andrewhun.finance.database;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class DatabaseInitializer {

    private static final String CREATE_USERS_TABLE = """
        CREATE TABLE IF NOT EXISTS Users (
            id            INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL,
            username      TEXT    UNIQUE NOT NULL,
            password_hash BLOB    NOT NULL,
            balance       REAL    NOT NULL DEFAULT 10000.0,
            is_logged_in  INTEGER NOT NULL DEFAULT 0,
            hash_salt     BLOB    NOT NULL
        )
        """;

    public void initialize() throws SQLException {
        createDatabaseDirectory();
        createUsersTable();
    }

    private void createDatabaseDirectory() throws SQLException {
        try {
            Files.createDirectories(DatabaseConnection.getDatabaseDirectory());
        }
        catch (IOException e) {
            throw new SQLException("Could not create database directory: " + e.getMessage(), e);
        }
    }

    private void createUsersTable() throws SQLException {
        DatabaseTemplate.execute(CREATE_USERS_TABLE);
    }
}