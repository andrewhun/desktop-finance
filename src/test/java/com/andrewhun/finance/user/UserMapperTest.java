package com.andrewhun.finance.user;

import com.andrewhun.finance.database.DatabaseConnection;
import com.andrewhun.finance.database.DatabaseExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DatabaseExtension.class)
public class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @BeforeEach
    void clearUsers() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Users");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='Users'");
        }
    }

    // ==================== Insert Tests ====================

    @Test
    @DisplayName("Should insert a new user and assign a generated id")
    void testSaveInsertsNewUserAndAssignsId() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertTrue(user.isNew());
        mapper.save(user);
        assertFalse(user.isNew());
        assertEquals(1, user.getId());
    }

    @Test
    @DisplayName("Should throw an exception when saving a user with a duplicate username")
    void testSaveDuplicateUsernameThrowsException() throws Exception {
        mapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        assertThrows(SQLException.class,
            () -> mapper.save(User.register("alice", "other", BigDecimal.valueOf(10000))));
    }

    // ==================== Update Tests ====================

    @Test
    @DisplayName("Should update an existing user")
    void testSaveUpdatesExistingUser() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        mapper.save(user);
        user.login();
        mapper.save(user);
        Optional<User> result = mapper.findById(user.getId());
        assertTrue(result.isPresent());
        assertTrue(result.get().isLoggedIn());
    }

    // ==================== Query Tests ====================

    @Test
    @DisplayName("Should return empty when no user is found by username")
    void testFindByUsernameReturnsEmptyWhenNotFound() throws SQLException {
        Optional<User> result = mapper.findByUsername("nobody");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the user when found by username")
    void testFindByUsernameReturnsUserWhenFound() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        mapper.save(user);
        Optional<User> result = mapper.findByUsername("alice");
        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when no user is found by id")
    void testFindByIdReturnsEmptyWhenNotFound() throws SQLException {
        Optional<User> result = mapper.findById(999);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when no user is logged in")
    void testFindLoggedInReturnsEmptyWhenNoOneLoggedIn() throws SQLException {
        Optional<User> result = mapper.findLoggedIn();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the logged-in user")
    void testFindLoggedInReturnsUserAfterLogin() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        mapper.save(user);
        user.login();
        mapper.save(user);
        Optional<User> result = mapper.findLoggedIn();
        assertTrue(result.isPresent());
        assertTrue(result.get().isLoggedIn());
    }

    // ==================== Delete Tests ====================

    @Test
    @DisplayName("Should remove a user by id")
    void testDeleteRemovesUser() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        mapper.save(user);
        mapper.delete(user.getId());
        assertTrue(mapper.findByUsername("alice").isEmpty());
    }

    @Test
    @DisplayName("Should not throw when deleting a non-existent id")
    void testDeleteNonExistentIdIsSafe() {
        assertDoesNotThrow(() -> mapper.delete(999));
    }

    // ==================== findAllUsernames Tests ====================

    @Test
    @DisplayName("Should return an empty list when no users exist")
    void testFindAllUsernamesReturnsEmptyListWhenNoUsers() throws SQLException {
        List<String> usernames = mapper.findAllUsernames();
        assertTrue(usernames.isEmpty());
    }

    @Test
    @DisplayName("Should return all usernames when users exist")
    void testFindAllUsernamesReturnsAllUsernames() throws Exception {
        mapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        mapper.save(User.register("bob", "password", BigDecimal.valueOf(10000)));
        List<String> usernames = mapper.findAllUsernames();
        assertEquals(2, usernames.size());
        assertTrue(usernames.contains("alice"));
        assertTrue(usernames.contains("bob"));
    }

    @Test
    @DisplayName("Should reflect newly added usernames immediately")
    void testFindAllUsernamesReflectsNewInserts() throws Exception {
        mapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        assertEquals(1, mapper.findAllUsernames().size());
        mapper.save(User.register("bob", "password", BigDecimal.valueOf(10000)));
        assertEquals(2, mapper.findAllUsernames().size());
    }

    // ==================== Round-Trip Tests ====================

    @Test
    @DisplayName("Should authenticate correctly after a save and reload")
    void testAuthenticationSurvivesRoundTrip() throws Exception {
        mapper.save(User.register("alice", "password", BigDecimal.valueOf(10000)));
        User reloaded = mapper.findByUsername("alice").orElseThrow();
        assertTrue(reloaded.authenticate("password"));
        assertFalse(reloaded.authenticate("wrong"));
    }

    @Test
    @DisplayName("Should preserve balance precision after a save and reload")
    void testBalancePrecisionPreservedAfterRoundTrip() throws Exception {
        BigDecimal balance = new BigDecimal("9999.99");
        mapper.save(User.register("alice", "password", balance));
        User reloaded = mapper.findByUsername("alice").orElseThrow();
        assertEquals(balance, reloaded.getBalance());
    }
}