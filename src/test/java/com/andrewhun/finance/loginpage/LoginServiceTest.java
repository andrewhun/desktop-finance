package com.andrewhun.finance.loginpage;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;
import com.andrewhun.finance.user.User;
import com.andrewhun.finance.usermapper.UserMapper;
import com.andrewhun.finance.usermapper.UserMapperFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private LoginService loginService;

    @BeforeEach
    void setUp() throws Exception {

        AppContext.setStorageMode(StorageMode.IN_MEMORY);
        UserMapper mapper = UserMapperFactory.create();
        User user = User.register("alice", "correctPass", BigDecimal.valueOf(10000));
        mapper.save(user);
        loginService = new LoginService();
    }

    @AfterEach
    void tearDown() {
        UserMapperFactory.reset();
        AppContext.reset();
    }

    @Test
    @DisplayName("Should report username is required when username is blank")
    void testEmptyUsername() {
        LoginResult result = loginService.attemptLogin("", "password123");
        assertEquals(LoginResult.Status.USERNAME_REQUIRED, result.getStatus());
    }

    @Test
    @DisplayName("Should report password is required when password is blank")
    void testEmptyPassword() {
        LoginResult result = loginService.attemptLogin("alice", "");
        assertEquals(LoginResult.Status.PASSWORD_REQUIRED, result.getStatus());
    }

    @Test
    @DisplayName("Should report username is required when both fields are blank")
    void testBothFieldsBlank() {
        LoginResult result = loginService.attemptLogin("", "");
        assertEquals(LoginResult.Status.USERNAME_REQUIRED, result.getStatus());
    }

    @Test
    @DisplayName("Should treat a whitespace-only username as blank")
    void testWhitespaceOnlyUsername() {
        LoginResult result = loginService.attemptLogin("   ", "password123");
        assertEquals(LoginResult.Status.USERNAME_REQUIRED, result.getStatus());
    }

    @Test
    @DisplayName("Should treat a whitespace-only password as blank")
    void testWhitespaceOnlyPassword() {
        LoginResult result = loginService.attemptLogin("alice", "   ");
        assertEquals(LoginResult.Status.PASSWORD_REQUIRED, result.getStatus());
    }

    @Test
    @DisplayName("Should report invalid credentials when the username does not exist")
    void testInvalidUsername() {
        LoginResult result = loginService.attemptLogin("nobody", "password123");
        assertEquals(LoginResult.Status.INVALID_CREDENTIALS, result.getStatus());
    }

    @Test
    @DisplayName("Should report invalid credentials when the password is incorrect")
    void testInvalidPassword() {
        LoginResult result = loginService.attemptLogin("alice", "wrongPass");
        assertEquals(LoginResult.Status.INVALID_CREDENTIALS, result.getStatus());
    }

    @Test
    @DisplayName("Should log the user in and return success on correct credentials")
    void testSuccessfulLogin() {
        LoginResult result = loginService.attemptLogin("alice", "correctPass");
        assertEquals(LoginResult.Status.SUCCESS, result.getStatus());
        assertTrue(result.getUser().isLoggedIn());
    }
}