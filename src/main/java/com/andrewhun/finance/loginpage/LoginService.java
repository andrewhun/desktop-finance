package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.user.User;
import com.andrewhun.finance.usermapper.UserMapper;
import com.andrewhun.finance.usermapper.UserMapperException;
import com.andrewhun.finance.usermapper.UserMapperFactory;

import java.security.GeneralSecurityException;
import java.util.Optional;

public class LoginService {

    private final UserMapper userMapper = UserMapperFactory.create();

    public LoginResult attemptLogin(String username, String password) {

        if (username.isBlank()) {
            return LoginResult.usernameRequired();
        }
        if (password.isBlank()) {
            return LoginResult.passwordRequired();
        }
        try {
            return authenticateAndLogin(username, password);
        }
        catch (UserMapperException | GeneralSecurityException e) {
            return LoginResult.error();
        }
    }

    private LoginResult authenticateAndLogin(String username, String password)
            throws GeneralSecurityException {

        Optional<User> maybeUser = userMapper.findByUsername(username);
        if (maybeUser.isEmpty() || !maybeUser.get().authenticate(password)) {
            return LoginResult.invalidCredentials();
        }
        User user = maybeUser.get();
        user.login();
        userMapper.save(user);
        return LoginResult.success(user);
    }
}