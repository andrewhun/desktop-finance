package com.andrewhun.finance.models;

import com.andrewhun.finance.exceptions.EntryNotFoundException;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.services.TestService;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.andrewhun.finance.security.PasswordEncryptionService;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;

class UserTest {

    private UserTableProcedures userTableProcedures = new UserTableProcedures();
    private Boolean isLoggedIn = false;
    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();
    private User testUser;
    private byte[] salt;
    private byte[] passwordHash;

    @BeforeEach
    void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
        salt = testUser.getHashSalt();
        passwordHash = testUser.getPasswordHash();
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
    void testSetPasswordHash() throws Exception {

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

    @Test
    void testAuthenticate() throws Exception {

        Assertions.assertTrue(testUser.authenticate(PASSWORD));
        Assertions.assertFalse(testUser.authenticate(INCORRECT_INPUT));
    }

    @Test
    void testCreateIncompleteEntry() throws Exception {

        User incompleteUser = User.createIncompleteEntry(USERNAME, PASSWORD, DEFAULT_BALANCE);

        Assertions.assertNull(incompleteUser.getId());
        Assertions.assertEquals(USERNAME, incompleteUser.getUsername());
        Assertions.assertTrue(incompleteUser.authenticate(PASSWORD));
        Assertions.assertEquals(DEFAULT_BALANCE, incompleteUser.getBalance());
        Assertions.assertFalse(incompleteUser.getIsLoggedIn());
    }

    @Test
    void testLoginAndLogout() throws Exception {

        testUser.login();
        Assertions.assertTrue(testUser.getIsLoggedIn());
        Assertions.assertTrue(userTableProcedures.findByUsername(USERNAME).getIsLoggedIn());

        testUser.logout();
        Assertions.assertFalse(testUser.getIsLoggedIn());
        Assertions.assertFalse(userTableProcedures.findByUsername(USERNAME).getIsLoggedIn());
    }

    @Test
    void testUpdateBalance() throws Exception {

        testUser.updateBalance(DEFAULT_BALANCE);
        Assertions.assertEquals(DEFAULT_BALANCE, testUser.getBalance());
        Assertions.assertEquals(DEFAULT_BALANCE, userTableProcedures.findByUsername(USERNAME).getBalance());
    }

    @Test void testGetCurrentUserId() throws Exception {

        testUser.login();
        Assertions.assertEquals(testUser.getId(), User.getCurrentUserId());
    }

    @Test void tetGetCurrentUserBalance() throws Exception {

        testUser.login();
        Assertions.assertEquals(testUser.getBalance(), User.getCurrentUserBalance());
    }

    @Test void testEntryNotFoundExceptionIsThrown() {

        assertThrows(EntryNotFoundException.class, User :: getCurrentUserId);
        assertThrows(EntryNotFoundException.class, User :: getCurrentUserBalance);
    }
}