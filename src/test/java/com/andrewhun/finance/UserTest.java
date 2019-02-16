/*
* This file contains the UserTest class, which is responsible for testing the User model.
* Since the User model is a simple data structure, the tests simply cover its getters, setters
* and constructor.
 */
package com.andrewhun.finance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import com.andrewhun.finance.models.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import com.andrewhun.finance.security.PasswordEncryptionService;

public class UserTest {

    private final Integer ID = 1;
    private final String USERNAME = "user";
    private final Double BALANCE = 500.00;
    private Boolean isLoggedIn = false;
    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
    private User testUser;
    private byte[] salt;
    private byte[] passwordHash;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {

        final String PASSWORD = "password";
        salt = passwordEncryptionService.generateSalt();
        passwordHash = passwordEncryptionService.getEncryptedPassword(PASSWORD, salt);
        testUser = new User(ID, USERNAME, passwordHash, BALANCE, isLoggedIn, salt);
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

        final String NOT_USER = "not_user";
        testUser.setUsername(NOT_USER);
        Assertions.assertNotEquals(USERNAME, testUser.getUsername());
        Assertions.assertEquals(NOT_USER, testUser.getUsername());
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

        Assertions.assertEquals(BALANCE, testUser.getBalance());
    }

    @Test
    void testSetBalance() {

        final Double NEW_BALANCE = 1000.00;
        testUser.setBalance(NEW_BALANCE);
        Assertions.assertNotEquals(BALANCE, testUser.getBalance());
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