package com.andrewhun.finance.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Supplies JDBC connections to the persistence layer. Implementations decide which
 * database engine (SQLite, MySQL, ...) and URL to use, which is the seam that lets the
 * application swap engines without touching {@link DatabaseTemplate} or the repositories.
 */
public interface ConnectionProvider {

    Connection getConnection() throws SQLException;

    /**
     * Prepares any storage the engine needs before connections are used (for example,
     * creating the directory that holds a SQLite file). No-op by default for engines that
     * manage their own storage, such as a remote MySQL server.
     */
    default void ensureStorageExists() throws SQLException {
    }
}