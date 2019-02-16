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
* These classes are simple data structures, meaning they don't perform any actions
* on their own (they don't have any procedures aside from the setters, getters and
* constructors). The general idea is that the program modifies the data inside the models,
* which in turn are used as parameters for the procedures of the corresponding
* "TableProcedures" classes. I have decided to implement this design after reading the
* 6th chapter of Clean Code (by "Uncle Bob" Martin), which is about Objects and Data Structures.
 */

package com.andrewhun.finance.models;

public class User {

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

    public User(String username, byte[] passwordHash, Double balance, byte[] hashSalt) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = balance;
        this.isLoggedIn = false;
        this.hashSalt = hashSalt;
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
}
