package com.andrewhun.finance.user;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;

public class User {

    private Integer id;
    private String username;
    private Password password;
    private BigDecimal balance;
    private boolean loggedIn;

    public User() {
    }

    public static User register(String username, String password, BigDecimal balance)
            throws GeneralSecurityException
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(Password.of(password));
        user.setBalance(balance);
        user.setLoggedIn(false);
        return user;
    }

    public boolean authenticate(String password) throws GeneralSecurityException {
        return this.password.verify(password);
    }

    public boolean isNew() {
        return id == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void adjustBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void login() {
        loggedIn = true;
    }

    public void logout() {
        loggedIn = false;
    }
}