package com.andrewhun.finance.usermapper;

import com.andrewhun.finance.config.AppContext;
import com.andrewhun.finance.config.StorageMode;
import com.andrewhun.finance.database.*;
import com.andrewhun.finance.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercises the JDBC mapper against a throwaway SQLite database in a per-test temp
 * directory. Tests the mapper in isolation — it constructs the mapper directly rather than
 * taking the one the composition root wires up.
 */
public class JdbcUserMapperTest {

    @TempDir
    Path tempDir;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws SQLException {

        AppContext.setStorageMode(StorageMode.TEST_DATABASE);
        DatabaseConfig.setTestDirectory(tempDir);
        DatabaseInitializer.initialize();
        userMapper = new JdbcUserMapper();
    }

    @AfterEach
    void tearDown() {
        AppContext.reset();
    }

    // ==================== Insert Tests ====================

    @Test
    @DisplayName("Should insert a new user and assign a generated id")
    void testInsertingNewUser() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertTrue(user.isNew());
        userMapper.save(user);
        assertFalse(user.isNew());
        assertEquals(1, user.getId());
    }

    @Test
    @DisplayName("Should throw an exception when saving a user with a duplicate username")
    void testInsertingDuplicateUser() throws Exception {
        userMapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        assertThrows(UserMapperException.class,
            () -> userMapper.save(User.register("alice", "other", BigDecimal.valueOf(10000))));
    }

    // ==================== Update Tests ====================

    @Test
    @DisplayName("Should update an existing user")
    void testUpdatingExistingUser() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        user.login();
        userMapper.save(user);
        Optional<User> result = userMapper.findById(user.getId());
        assertTrue(result.isPresent());
        assertTrue(result.get().isLoggedIn());
    }

    // ==================== Query Tests ====================

    @Test
    @DisplayName("Should return empty when no user is found by username")
    void testFindingNoUsersByName() {
        Optional<User> result = userMapper.findByUsername("nobody");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the user when found by username")
    void testFindingExistingUserByName() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        Optional<User> result = userMapper.findByUsername("alice");
        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when no user is found by id")
    void testFindingNoUsersById() {
        Optional<User> result = userMapper.findById(999);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the user when found by id")
    void testFindingExistingUserById() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        Optional<User> result = userMapper.findById(user.getId());
        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when no user is logged in")
    void testFindingNoLoggedInUsers() {
        Optional<User> result = userMapper.findLoggedIn();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the logged-in user")
    void testFindingTheLoggedInUser() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        user.login();
        userMapper.save(user);
        Optional<User> result = userMapper.findLoggedIn();
        assertTrue(result.isPresent());
        assertTrue(result.get().isLoggedIn());
    }

    // ==================== Delete Tests ====================

    @Test
    @DisplayName("Should remove a user by id")
    void testDeletingUser() throws Exception {

        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        userMapper.save(user);
        userMapper.delete(user.getId());
        assertTrue(userMapper.findByUsername("alice").isEmpty());
    }

    @Test
    @DisplayName("Should not throw when deleting a non-existent user")
    void testDeletingNonExistentUser() {
        assertDoesNotThrow(() -> userMapper.delete(999));
    }

    // ==================== findAllUsernames Tests ====================

    @Test
    @DisplayName("Should return an empty list when no users exist")
    void testFindingNoMatchingUsersByName() {
        List<String> usernames = userMapper.findAllUsernames();
        assertTrue(usernames.isEmpty());
    }

    @Test
    @DisplayName("Should return all usernames when users exist")
    void testFindingAllMatchingUsersByName() throws Exception {

        userMapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        userMapper.save(User.register("bob", "password", BigDecimal.valueOf(10000)));
        List<String> usernames = userMapper.findAllUsernames();
        assertEquals(2, usernames.size());
        assertTrue(usernames.contains("alice"));
        assertTrue(usernames.contains("bob"));
    }

    @Test
    @DisplayName("Should reflect newly added usernames immediately")
    void testFindingAllMatchingUsersByNameAfterInsert() throws Exception {

        userMapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        assertEquals(1, userMapper.findAllUsernames().size());
        userMapper.save(User.register("bob", "password", BigDecimal.valueOf(10000)));
        assertEquals(2, userMapper.findAllUsernames().size());
    }

    // ==================== Round-Trip Tests ====================

    @Test
    @DisplayName("Should authenticate correctly after a save and reload")
    void testAuthenticatingAfterReload() throws Exception {

        userMapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        User reloaded = userMapper.findByUsername("alice").orElseThrow();
        assertTrue(reloaded.authenticate("password"));
        assertFalse(reloaded.authenticate("wrong"));
    }

    @Test
    @DisplayName("Should preserve balance precision after a save and reload")
    void testBalanceAfterReload() throws Exception {

        BigDecimal balance = new BigDecimal("9999.99");
        userMapper.save(User.register("alice", "password", balance));
        User reloaded = userMapper.findByUsername("alice").orElseThrow();
        assertEquals(balance, reloaded.getBalance());
    }
}