package com.andrewhun.finance.user;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;

public class User {

    Integer id;
    private final String username;
    Password password;
    private BigDecimal balance;
    private boolean loggedIn;

    private User(Integer id, String username, Password password, BigDecimal balance, boolean loggedIn) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.loggedIn = loggedIn;
    }

    public static User register(String username, String password, BigDecimal balance)
            throws GeneralSecurityException {
        return new User(null, username, Password.of(password), balance, false);
    }

    static User reconstitute(Integer id, String username, Password password, BigDecimal balance, boolean loggedIn) {
        return new User(id, username, password, balance, loggedIn);
    }

    public boolean authenticate(String password) throws GeneralSecurityException {
        return this.password.verify(password);
    }

    public void login() {
        loggedIn = true;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isNew() {
        return id == null;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void adjustBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}