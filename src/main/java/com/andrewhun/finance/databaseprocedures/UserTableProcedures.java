/*
* This file contains the UserTableProcedures class. As the name suggests,
* this class holds the stored procedures that are connected to the Users table
* of the database. This class mostly interacts with the User model (models package)
* and the Users table in the database.
*
* You will find a lot of repetition in the methods of this class. I have three excuses for them.
* First, preparing statements using the "?" syntax protects the database against SQL injection
* attacks. This functionality would be lost if I were to use generic queries (by directly entering
* variables into the queries).
* The second excuse is a bit harder to explain. It has to do with how the SQLite database operates.
* With SQLite, only one thread can write in the database at any given time. Basically, I have to ensure
* that the connection to the database is open for only as long as necessary. This means you will see a lot
* of set up and tear down related to the database connection.
* The third excuse is the way ResultSets work. They require an open database connection. As soon as you
* close the connection, they get deleted. This means that functions that need to do something with
* ResultSets must do their work between the connection setup and tear down elements.
* The combination of these factors resulted in repetitive and bigger than desired functions.
*
* I considered relying on Spring, which supposedly handles all three issues well. I also thought about switching
* to MySQL. I want to keep dependencies to a minimum, however, and Spring is a bit too heavy for my liking.
* As for MySQL, I feel like using it would just hide the fact that I cannot use JDBC effectively.
 */

package com.andrewhun.finance.databaseprocedures;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.exceptions.UserNotFoundException;

public class UserTableProcedures extends StoredProceduresBaseClass {

    public void addUserToDatabase(User incompleteUserModel) throws SQLException {

        connectToDatabase();
        String queryScript = "INSERT INTO Users(username, password_hash, balance, hash_salt) " +
                "VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, incompleteUserModel.getUsername());
        preparedStatement.setBytes(2, incompleteUserModel.getPasswordHash());
        preparedStatement.setDouble(3, incompleteUserModel.getBalance());
        preparedStatement.setBytes(4, incompleteUserModel.getHashSalt());
        preparedStatement.executeUpdate();

        disconnectFromDatabase();
    }

    public void changeUserLoginStatus(User user) throws SQLException {

        connectToDatabase();
        String queryScript = "UPDATE Users SET is_logged_in = ?" +
                " WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setBoolean(1, user.getIsLoggedIn());
        preparedStatement.setInt(2, user.getId());
        preparedStatement.executeUpdate();

        disconnectFromDatabase();
    }

    public User getUserById(Integer id) throws SQLException, UserNotFoundException {

        connectToDatabase();
        String queryScript = "SELECT * FROM Users WHERE id = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        User selectedUser = getUser(resultSet);
        disconnectFromDatabase();
        return selectedUser;
    }

    public User getUserByUsername(String username) throws SQLException, UserNotFoundException {

        connectToDatabase();
        String queryScript = "SELECT * FROM Users WHERE username = ?";
        preparedStatement = connection.prepareStatement(queryScript);
        preparedStatement.setString(1, username);
        resultSet = preparedStatement.executeQuery();

        User selectedUser = getUser(resultSet);
        disconnectFromDatabase();
        return selectedUser;
    }

    public User getLoggedInUser() throws SQLException, UserNotFoundException {

        connectToDatabase();
        statement = connection.createStatement();
        String queryScript = "SELECT * FROM Users WHERE is_logged_in = true";
        resultSet = statement.executeQuery(queryScript);

        User loggedInUser = getUser(resultSet);
        disconnectFromDatabase();
        return loggedInUser;
    }

    private User getUser(ResultSet resultSet) throws SQLException, UserNotFoundException {

        if (resultSet.next()) {

            return createUserModelFromResultSet(resultSet);
        }
        throw new UserNotFoundException("Could not find user");
    }

    private User createUserModelFromResultSet(ResultSet resultSet) throws SQLException {

        Integer id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        byte[] passwordHash = resultSet.getBytes("password_hash");
        Double balance = resultSet.getDouble("balance");
        Boolean isLoggedIn = resultSet.getBoolean("is_logged_in");
        byte[] hashSalt = resultSet.getBytes("hash_salt");
        return new User(id, username, passwordHash, balance, isLoggedIn, hashSalt);
    }

    public Boolean aUserIsLoggedIn() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        String queryScript = "SELECT * FROM Users WHERE is_logged_in = true";
        resultSet = statement.executeQuery(queryScript);
        Boolean aUserIsLoggedIn = resultSet.next();

        disconnectFromDatabase();
        return aUserIsLoggedIn;
    }


    public List<String> getListOfUsernames() throws SQLException {

        connectToDatabase();
        statement = connection.createStatement();
        String queryScript = "SELECT username FROM Users";
        resultSet = statement.executeQuery(queryScript);

        List<String> usernames = new ArrayList<>();
        while(resultSet.next()) {

            usernames.add(resultSet.getString("username"));
        }
        disconnectFromDatabase();
        return usernames;
    }
}