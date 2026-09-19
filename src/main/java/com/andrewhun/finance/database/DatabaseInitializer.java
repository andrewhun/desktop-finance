package com.andrewhun.finance.database;

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

    public static void initialize() throws SQLException {

        DatabaseTemplate template = new DatabaseTemplate();
        ConnectionProvider provider = template.getConnectionProvider();
        provider.ensureStorageExists();
        createUsersTable(template);
    }

    private static void createUsersTable(DatabaseTemplate template) throws SQLException {
        template.execute(CREATE_USERS_TABLE);
    }
}