/*
* This file contains the TestProcedures class, which contains the stored procedures
* that are used in the testing process.
 */

package com.andrewhun.finance.databaseprocedures;

import java.sql.SQLException;
import com.andrewhun.finance.models.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import com.andrewhun.finance.security.PasswordEncryptionService;

public class TestProcedures extends StoredProceduresBaseClass {

    public void addTestUserToDatabase(String username) throws SQLException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        User testUser = createTestUser(username);
        connectToDatabase();
        String queryScript = "INSERT INTO Users(username, password_hash, balance," +
                " hash_salt) VALUES(?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, testUser.getUsername());
        preparedStatement.setBytes(2, testUser.getPasswordHash());
        preparedStatement.setDouble(3, testUser.getBalance());
        preparedStatement.setBytes(4, testUser.getHashSalt());
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public User createTestUser(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {

        final String PASSWORD = "testPass";
        final Double BALANCE = 500.00;

        PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
        byte[] salt = passwordEncryptionService.generateSalt();
        byte[] passwordHash = passwordEncryptionService.getEncryptedPassword(PASSWORD, salt);

        return new User(username, passwordHash, BALANCE, salt);
    }

    public void removeTestUserFromDatabase(String username) throws SQLException {

        connectToDatabase();
        String queryScript = "DELETE FROM Users WHERE username = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
        disconnectFromDatabase();
    }

    public void createUsersTable() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        String queryScript = "CREATE TABLE Users (\n"
                + "id INTEGER PRIMARY KEY ASC AUTOINCREMENT\n"
                + "UNIQUE\n"
                + "NOT NULL\n"
                + "DEFAULT (1),\n"
                + "username STRING UNIQUE\n"
                + "NOT NULL,\n"
                + "password_hash BLOB NOT NULL,\n"
                + "balance DECIMAL NOT NULL\n"
                + "DEFAULT (10000.0),\n"
                + "is_logged_in BOOLEAN NOT NULL \n"
                + "DEFAULT (false), \n"
                + "hash_salt BLOB NOT NULL \n"
                + ");";
        statement.executeUpdate(queryScript);
        disconnectFromDatabase();
    }

    public void dropUsersTable() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        String queryScript = "DROP TABLE IF EXISTS Users";
        statement.executeUpdate(queryScript);
        disconnectFromDatabase();

    }
}
