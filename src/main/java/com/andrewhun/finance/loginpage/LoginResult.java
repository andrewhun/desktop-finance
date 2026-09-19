package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.user.User;

public class LoginResult {

    public enum Status {
        SUCCESS,
        USERNAME_REQUIRED,
        PASSWORD_REQUIRED,
        INVALID_CREDENTIALS,
        ERROR
    }

    private final Status status;
    private final User user;

    private LoginResult(Status status, User user) {
        this.status = status;
        this.user = user;
    }

    private LoginResult(Status status) {
        this.status = status;
        this.user = null;
    }

    static LoginResult success(User user) {
        return new LoginResult(Status.SUCCESS, user);
    }

    static LoginResult usernameRequired() {
        return new LoginResult(Status.USERNAME_REQUIRED);
    }

    static LoginResult passwordRequired() {
        return new LoginResult(Status.PASSWORD_REQUIRED);
    }

    static LoginResult invalidCredentials() {
        return new LoginResult(Status.INVALID_CREDENTIALS);
    }

    static LoginResult error() {
        return new LoginResult(Status.ERROR);
    }

    public Status getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}