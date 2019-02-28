package com.andrewhun.finance.models;

import org.junit.jupiter.api.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import static com.andrewhun.finance.util.NamedConstants.*;
import com.andrewhun.finance.security.PasswordEncryptionService;

class UserTest {

    private Boolean isLoggedIn = false;
    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
    private User testUser;
    private byte[] salt;
    private byte[] passwordHash;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {

        salt = passwordEncryptionService.generateSalt();
        passwordHash = passwordEncryptionService.getEncryptedPassword(PASSWORD, salt);
        testUser = new User(ID, USERNAME, passwordHash, FIVE_HUNDRED, isLoggedIn, salt);
    }

    @Test
    void testGetId() {

        Assertions.assertEquals(ID, testUser.getId());
    }

    @Test
    void testGetUsername() {

        Assertions.assertEquals(USERNAME, testUser.getUsername());
    }

    @Test
    void testSetUsername() {

        testUser.setUsername(SECOND_USERNAME);
        Assertions.assertNotEquals(USERNAME, testUser.getUsername());
        Assertions.assertEquals(SECOND_USERNAME, testUser.getUsername());
    }

    @Test
    void testGetPasswordHash() {

        Assertions.assertEquals(passwordHash, testUser.getPasswordHash());
    }

    @Test
    void testSetPasswordHash() throws NoSuchAlgorithmException, InvalidKeySpecException {

        final String NEW_PASSWORD = "new";
        byte[] newEncryptedPassword = passwordEncryptionService.getEncryptedPassword(NEW_PASSWORD, salt);
        testUser.setPasswordHash(newEncryptedPassword);
        Assertions.assertNotEquals(passwordHash, testUser.getPasswordHash());
        Assertions.assertEquals(newEncryptedPassword, testUser.getPasswordHash());
    }

    @Test
    void testGetBalance() {

        Assertions.assertEquals(FIVE_HUNDRED, testUser.getBalance());
    }

    @Test
    void testSetBalance() {

        final Double NEW_BALANCE = 1000.00;
        testUser.setBalance(NEW_BALANCE);
        Assertions.assertNotEquals(FIVE_HUNDRED, testUser.getBalance());
        Assertions.assertEquals(NEW_BALANCE, testUser.getBalance());
    }

    @Test
    void testGetIsLoggedIn() {

        Assertions.assertEquals(isLoggedIn, testUser.getIsLoggedIn());
    }

    @Test
    void testSetIsLoggedIn() {

        isLoggedIn = true;
        testUser.setIsLoggedIn(isLoggedIn);
        Assertions.assertNotEquals(false, testUser.getIsLoggedIn());
        Assertions.assertEquals(isLoggedIn, testUser.getIsLoggedIn());
    }

    @Test
    void testGetSalt() {

        Assertions.assertEquals(salt, testUser.getHashSalt());
    }
}