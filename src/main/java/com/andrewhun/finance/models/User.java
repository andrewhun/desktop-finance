/*
* This file contains the User class. Each instance of this class represents an
* entry in the Users table. My intention with this class (and the other classes in
* this package) was to provide a simple way to interact with the database.
* My inspiration mainly stems from using Python's Django framework, which
* has a very similar system (of course, its much more advanced than my
* implementation). Some credit also goes to the person behind the MVP Java
* YouTube channel. I saw a similar implementation in one of his videos, and
* it reminded me that "it can be done this way, too".
*
* The User class is somewhat special. While the rest of these "Model" classes
* mostly serve as containers for data, this class has functionality attached to it.
* Namely, you will find the authenticate, login and logout functions below.
* My reasoning behind this arrangement is that users are special entities in this program.
* Many other aspects of the program rely on them. Thus, I think it is necessary to add
* these features to the class.
 */

package com.andrewhun.finance.models;

import java.sql.SQLException;
import com.andrewhun.finance.security.PasswordEncryptionService;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

public class User {

    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
    private UserTableProcedures userTableProcedures = new UserTableProcedures();

    private Integer id;
    private String username;
    private byte[] passwordHash;
    private Double balance;
    private Boolean isLoggedIn;
    private byte[] hashSalt;

    public User(Integer id, String username, byte[] passwordHash, Double balance,
                Boolean isLoggedIn, byte[] hashSalt) {

        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = balance;
        this.isLoggedIn = isLoggedIn;
        this.hashSalt = hashSalt;
    }

    public static User createIncompleteEntry(String username, String password, Double balance) throws Exception {

        return new User(username, password, balance);
    }

    private User(String username, String password, Double balance) throws Exception {

        this.username = username;

        this.hashSalt = passwordEncryptionService.generateSalt();
        this.passwordHash = passwordEncryptionService.getEncryptedPassword(password, this.hashSalt);
        this.balance = balance;
        this.isLoggedIn = false;
    }

    public User() {}

    public Boolean authenticate(String attemptedPassword) throws Exception {

        return passwordEncryptionService.authenticate(attemptedPassword, this.passwordHash, this.hashSalt);
    }

    public void updateBalance(Double balance) throws SQLException {

        this.setBalance(balance);
        userTableProcedures.changeUserBalance(this);
    }

    public void login() throws Exception {

        this.isLoggedIn = true;
        userTableProcedures.changeUserLoginStatus(this);
    }

    public void logout() throws Exception {

        this.isLoggedIn = false;
        userTableProcedures.changeUserLoginStatus(this);
    }

    public Integer getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String newUsername) {

        username = newUsername;
    }

    public byte[] getPasswordHash() {

        return passwordHash;
    }

    public void setPasswordHash(byte[] newPasswordHash) {

        passwordHash = newPasswordHash;
    }

    public Double getBalance() {

        return balance;
    }

    public void setBalance(Double newBalance) {

        balance = newBalance;
    }

    public Boolean getIsLoggedIn() {

        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {

        this.isLoggedIn = isLoggedIn;
    }

    public byte[] getHashSalt() {

        return hashSalt;
    }

    public void setHashSalt(byte[] salt) {

        this.hashSalt = salt;
    }
}