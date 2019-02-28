package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import com.andrewhun.finance.services.TestService;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.EntryNotFoundException;

class UserTableProceduresTest {

    private static UserTableProcedures userTableProcedures = new UserTableProcedures();
    private static User testUser;

    @BeforeAll
    static void setUp() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @Test
    void testGettingUserByUsername() throws Exception {

        try {
            User selectedUser = userTableProcedures.getUserByUsername(USERNAME);
            Assertions.assertEquals(ID, selectedUser.getId());
        }
        catch (EntryNotFoundException e) {

            fail("Problems with the set up for the tests. Test user not found in database.");
        }
    }

    @Test
    void testGettingUserById() throws Exception {

        try {
            User selectedUser = userTableProcedures.getUserById(ID);
            Assertions.assertEquals(USERNAME, selectedUser.getUsername());
        }
        catch (EntryNotFoundException e) {

            fail("Problems with the set up for the tests. Test user not found in database.");
        }
    }

    /* I'm using multiple User models to avoid relying on the "aUserIsLoggedIn" function
    * of the UserTableProcedures class, and I'm not using the login/logout functions
    * of this test class because that would hide the function that is being tested.
    * Having to use these workarounds is proof that the design of these routines could
    * be better. ChangeUserLoginStatus and aUserIsLoggedIn seem to be strongly coupled.*/
    @Test
    void testChangingLoginStatus() throws Exception {

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
    void testAUserIsLoggedIn() throws SQLException {

        logoutTestUser();
        Assertions.assertFalse(userTableProcedures.aUserIsLoggedIn());
        loginTestUser();
        Assertions.assertTrue(userTableProcedures.aUserIsLoggedIn());
    }

    @Test
    void testThatGetIsLoggedInThrowsNotFoundException() throws SQLException {
        logoutTestUser();
        assertThrows(EntryNotFoundException.class, () -> userTableProcedures.getLoggedInUser());
    }

    @Test
    void testThatLoggedInUserIsFound() throws Exception {

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
    void testAddingNewUser() throws Exception {

        User incompleteUserModel = TestService.createTestUser(SECOND_USERNAME);
        userTableProcedures.addUserToDatabase(incompleteUserModel);

        try {
            User secondTestUser = userTableProcedures.getUserById(SECOND_ID);
            Assertions.assertEquals(SECOND_USERNAME, secondTestUser.getUsername());

            List<String> usernames = userTableProcedures.getListOfUsernames();
            Assertions.assertTrue(usernames.contains(SECOND_USERNAME));
        }
        catch (EntryNotFoundException e) {

            fail("User was not added to the database.");
        }
        finally {
            TestService.deleteTestUser(SECOND_USERNAME);
        }
    }

    private void loginTestUser() throws SQLException {

        testUser.setIsLoggedIn(true);
        userTableProcedures.changeUserLoginStatus(testUser);
    }

    private void logoutTestUser() throws SQLException {

        testUser.setIsLoggedIn(false);
        userTableProcedures.changeUserLoginStatus(testUser);
    }
}