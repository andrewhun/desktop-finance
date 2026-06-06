package com.andrewhun.finance.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    // ==================== Registration Tests ====================

    @Test
    @DisplayName("Should have a null id when first registered")
    void testNewUserHasNullId() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertTrue(user.isNew());
        assertNull(user.getId());
    }

    // ==================== Reconstitution Tests ====================

    @Test
    @DisplayName("Should not be considered new after reconstitution")
    void testReconstituteHasNonNullId() throws Exception {
        Password password = Password.of("password");
        User user = User.reconstitute(1, "alice", password, BigDecimal.valueOf(10000), false);
        assertFalse(user.isNew());
        assertEquals(1, user.getId());
    }

    // ==================== Authentication Tests ====================

    @Test
    @DisplayName("Should authenticate with the correct password")
    void testAuthenticateWithCorrectPassword() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertTrue(user.authenticate("password"));
    }

    @Test
    @DisplayName("Should reject authentication with the wrong password")
    void testAuthenticateWithWrongPassword() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertFalse(user.authenticate("wrong"));
    }

    // ==================== Login State Tests ====================

    @Test
    @DisplayName("Should set logged in to true after login")
    void testLoginSetsLoggedInTrue() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        assertFalse(user.isLoggedIn());
        user.login();
        assertTrue(user.isLoggedIn());
    }

    @Test
    @DisplayName("Should set logged in to false after logout")
    void testLogoutSetsLoggedInFalse() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        user.login();
        user.logout();
        assertFalse(user.isLoggedIn());
    }

    @Test
    @DisplayName("Should remain logged out when logout is called without a prior login")
    void testLogoutIsIdempotent() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        user.logout();
        assertFalse(user.isLoggedIn());
    }

    // ==================== Balance Adjustment Tests ====================

    @Test
    @DisplayName("Should increase balance when adjusted by a positive amount")
    void testAdjustBalanceIncrease() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        user.adjustBalance(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(10500), user.getBalance());
    }

    @Test
    @DisplayName("Should decrease balance when adjusted by a negative amount")
    void testAdjustBalanceDecrease() throws Exception {
        User user = User.register("alice", "password", BigDecimal.valueOf(10000));
        user.adjustBalance(BigDecimal.valueOf(-200));
        assertEquals(BigDecimal.valueOf(9800), user.getBalance());
    }
}