/*
* This file contains the UserTableProceduresTest class which is responsible for
* testing the stored procedures connected to the Users table of the database.
 */

package com.andrewhun.finance;

import java.util.List;
import java.sql.SQLException;

import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.UserNotFoundException;
import com.andrewhun.finance.databaseprocedures.ActiveDatabase;
import com.andrewhun.finance.databaseprocedures.TestProcedures;
import com.andrewhun.finance.databaseprocedures.UserTableProcedures;
import com.andrewhun.finance.databaseprocedures.StoredProceduresBaseClass;

class UserTableProceduresTest {

    private static final String USERNAME = "test";
    private static TestProcedures testProcedures = new TestProcedures();
    private static UserTableProcedures userTableProcedures = new UserTableProcedures();
    private final Integer ID = 1;
    private User testUser;

    @BeforeAll
    static void setUp() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        StoredProceduresBaseClass.activeDatabase = ActiveDatabase.TEST;
        testProcedures.createUsersTable();
        testProcedures.addTestUserToDatabase(USERNAME);
    }

    @AfterAll
    static void removeUsersTable() throws SQLException {

        testProcedures.dropUsersTable();
    }

    @Test
    void testGettingUserByUsername() throws SQLException {

        try {
            User selectedUser = userTableProcedures.getUserByUsername(USERNAME);
            Assertions.assertEquals(ID, selectedUser.getId());
        }
        catch (UserNotFoundException e) {

            fail("Problems with the set up for the tests. Test user not found in database.");
        }
    }

    @Test
    void testGettingUserById() throws SQLException {

        try {
            User selectedUser = userTableProcedures.getUserById(ID);
            Assertions.assertEquals(USERNAME, selectedUser.getUsername());
        }
        catch (UserNotFoundException e) {

            fail("Problems with the set up for the tests. Test user not found in database.");
        }
    }

    /* I'm using multiple User models to avoid relying on the "aUserIsLoggedIn" function
    * of the UserTableProcedures class, and I'm not using the login/logout functions
    * of this test class because that would hide the function that is being tested.
    * Having to use these workarounds is proof that the design of these routines could
    * be better. ChangeUserLoginStatus and aUserIsLoggedIn seem to be strongly coupled.*/
    @Test
    void testChangingLoginStatus() throws SQLException, UserNotFoundException {

        User testUser = userTableProcedures.getUserByUsername(USERNAME);
        testUser.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(testUser);

        User newTestUserModel = userTableProcedures.getUserByUsername(USERNAME);
        Assertions.assertTrue(newTestUserModel.getIsLoggedIn());

        testUser.setIsLoggedIn(false);
        userTableProcedures.changeUserLoginStatus(testUser);

        User anotherTestUserModel = userTableProcedures.getUserByUsername(USERNAME);
        Assertions.assertFalse(anotherTestUserModel.getIsLoggedIn());
    }

    @Test
    void testAUserIsLoggedIn() throws SQLException, UserNotFoundException {

        logoutTestUser();
        Assertions.assertFalse(userTableProcedures.aUserIsLoggedIn());
        loginTestUser();
        Assertions.assertTrue(userTableProcedures.aUserIsLoggedIn());
    }

    @Test
    void testThatGetIsLoggedInThrowsNotFoundException() throws SQLException, UserNotFoundException {
        logoutTestUser();
        assertThrows(UserNotFoundException.class, () -> userTableProcedures.getLoggedInUser());
    }

    @Test
    void testThatLoggedInUserIsFound() throws SQLException, UserNotFoundException {

        loginTestUser();
        User loggedInUser = userTableProcedures.getLoggedInUser();
        Assertions.assertEquals(testUser.getUsername(), loggedInUser.getUsername());
    }

    @Test
    void testGettingUsernames() throws SQLException {

        List<String> usernames = userTableProcedures.getListOfUsernames();
        Assertions.assertTrue(usernames.contains(USERNAME));
    }

    @Test
    void testAddingNewUser() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {

        String username = "test2";
        User incompleteUserModel = testProcedures.createTestUser(username);
        userTableProcedures.addUserToDatabase(incompleteUserModel);

        try {
            User secondTestUser = userTableProcedures.getUserById(2);
            Assertions.assertEquals(username, secondTestUser.getUsername());

            List<String> usernames = userTableProcedures.getListOfUsernames();
            Assertions.assertTrue(usernames.contains(username));
        }
        catch (UserNotFoundException e) {

            fail("User was not added to the database.");
        }
        finally {
            testProcedures.removeTestUserFromDatabase(username);
        }
    }

    private void loginTestUser() throws SQLException, UserNotFoundException {

        testUser = userTableProcedures.getUserByUsername(USERNAME);
        testUser.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(testUser);
    }

    private void logoutTestUser() throws SQLException, UserNotFoundException {

        testUser = userTableProcedures.getUserByUsername(USERNAME);
        testUser.setIsLoggedIn(false);
        userTableProcedures.changeUserLoginStatus(testUser);
    }
}