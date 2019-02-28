/*
* This file contains the UserService class, which is handles the communication
* between the "front-end" operations and the database operations that are related
* to the Users table. The purpose of this service layer in general is to separate
* the "business logic" from database concerns.
 */

package com.andrewhun.finance.services;

import com.andrewhun.finance.models.User;
import com.andrewhun.finance.security.PasswordEncryptionService;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

import java.sql.SQLException;

public class UserService {

    private UserTableProcedures userTableProcedures = new UserTableProcedures();
    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();

    public void addUserToDatabase(String username, String password, Double balance) throws Exception {

        userTableProcedures.addUserToDatabase(createIncompleteUserObject(username, password, balance));
    }

    User createIncompleteUserObject(String username, String password, Double balance)
            throws Exception {

       User user = getUserWithSaltAndPasswordHash(password);
       user.setUsername(username);
       user.setBalance(balance);
       user.setIsLoggedIn(false);
       return user;
    }

    private User getUserWithSaltAndPasswordHash(String password) throws Exception {

        User user = new User();

        user.setHashSalt(passwordEncryptionService.generateSalt());
        byte[] passwordHash = passwordEncryptionService.getEncryptedPassword(password, user.getHashSalt());
        user.setPasswordHash(passwordHash);
        return user;
    }

    public void loginUser(User user) throws SQLException {

        user.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(user);
    }

    public void logoutUser(User user) throws SQLException {

        user.setIsLoggedIn(false);
        userTableProcedures.changeUserLoginStatus(user);
    }

    public User getCurrentUser() throws Exception {

        return userTableProcedures.getLoggedInUser();
    }

    public Boolean authenticate(User user, String password) throws Exception {

        return passwordEncryptionService.authenticate(password, user.getPasswordHash(), user.getHashSalt());
    }

    public User findById(Integer id) throws Exception {

        return userTableProcedures.getUserById(id);
    }

    public User findByUsername(String username) throws Exception {

        return userTableProcedures.getUserByUsername(username);
    }

    public Boolean usernameIsTaken(String username) throws SQLException {

        return userTableProcedures.getListOfUsernames().contains(username);
    }

    public Integer getNumberOfUsers() throws SQLException {

        return userTableProcedures.getListOfUsernames().size();
    }

    public void deleteUser(User user) throws SQLException {

        userTableProcedures.deleteUser(user.getUsername());
    }
}