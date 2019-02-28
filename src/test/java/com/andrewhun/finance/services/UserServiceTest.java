package com.andrewhun.finance.services;

import java.sql.SQLException;
import org.junit.jupiter.api.*;
import com.andrewhun.finance.models.User;
import static org.junit.jupiter.api.Assertions.fail;
import static com.andrewhun.finance.util.NamedConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.andrewhun.finance.exceptions.EntryNotFoundException;

class UserServiceTest {

    private static UserService userService = new UserService();
    private static User testUser;

    @BeforeAll
    static void setUpTestDatabase() throws Exception {

        testUser = TestService.setUpTestDatabaseWithTestUser();
    }

    @BeforeEach
    void logoutTestUser() throws SQLException {

        userService.logoutUser(testUser);
    }

    @Test
    void testFindById() throws Exception {

        User selectedUser = userService.findById(ID);
        Assertions.assertEquals(selectedUser.getUsername(), testUser.getUsername());
    }

    @Test
    void testFindByUsername() throws Exception {

        User selectedUser = userService.findByUsername(USERNAME);
        Assertions.assertEquals(ID ,selectedUser.getId());
    }

    @Test
    void testLookingForUserThatDoesNotExist() {

        assertThrows(EntryNotFoundException.class,
                () -> userService.findById(SECOND_ID));
        assertThrows(EntryNotFoundException.class,
                () -> userService.findByUsername(SECOND_USERNAME));
    }

    @Test
    void testAddingUserToDatabase() throws Exception {

        userService.addUserToDatabase(SECOND_USERNAME, PASSWORD, THREE_HUNDRED);
        Assertions.assertNotNull(userService.findByUsername(SECOND_USERNAME));
        TestService.deleteTestUser(SECOND_USERNAME);
    }

    @Test
    void testCreatingIncompleteUser() throws Exception {

        User incompleteUser = userService.createIncompleteUserObject(SECOND_USERNAME, PASSWORD, THREE_HUNDRED);
        Assertions.assertNull(incompleteUser.getId());
        Assertions.assertEquals(SECOND_USERNAME, incompleteUser.getUsername());
        Assertions.assertTrue(userService.authenticate(incompleteUser, PASSWORD));
        Assertions.assertEquals(THREE_HUNDRED, incompleteUser.getBalance());
        Assertions.assertFalse(incompleteUser.getIsLoggedIn());
    }

    @Test
    void testGettingCurrentUser() throws Exception {

        assertThrows(EntryNotFoundException.class,
                () -> userService.getCurrentUser());

        userService.loginUser(testUser);
        User currentUser = userService.getCurrentUser();
        Assertions.assertEquals(USERNAME, currentUser.getUsername());

    }

    @Test
    void testLoggingInUser() throws Exception {

        userService.loginUser(testUser);
        Assertions.assertTrue(testUser.getIsLoggedIn());
        try {
            User loggedInUser = userService.getCurrentUser();
            Assertions.assertEquals(USERNAME, loggedInUser.getUsername());
        }
        catch (EntryNotFoundException e) {

            fail("Did not log user in");
        }
    }

    @Test
    void testLoggingOutUser() throws Exception {

        userService.loginUser(testUser);
        userService.logoutUser(testUser);
        try {
            userService.getCurrentUser();
            fail("Did not log user out");
        }
        catch (EntryNotFoundException ignore) {}
    }

    @Test
    void testAuthentication() throws Exception {

        Assertions.assertTrue(userService.authenticate(testUser, PASSWORD));
        Assertions.assertFalse(userService.authenticate(testUser, INCORRECT_INPUT));
    }

    @Test
    void testUsernameIsTaken() throws SQLException {

        Assertions.assertTrue(userService.usernameIsTaken(USERNAME));
        Assertions.assertFalse(userService.usernameIsTaken(INCORRECT_INPUT));
    }

    @Test
    void testGettingNumberOfUsers() throws Exception {

        Assertions.assertEquals(1, (int)userService.getNumberOfUsers());
        TestService.addTestUserToDatabase(SECOND_USERNAME);
        Assertions.assertEquals(2, (int)userService.getNumberOfUsers());
        TestService.deleteTestUser(SECOND_USERNAME);
    }

    @Test
    void testDeletingUser() throws Exception {

        TestService.addTestUserToDatabase(SECOND_USERNAME);
        User secondUser = userService.findByUsername(SECOND_USERNAME);
        userService.deleteUser(secondUser);

        try {
            userService.findByUsername(SECOND_USERNAME);
            fail("Did not delete user");
        }
        catch (EntryNotFoundException ignore) {}
    }
}