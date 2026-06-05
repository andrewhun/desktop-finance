package com.andrewhun.finance.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

    // ==================== Salt Generation Tests ====================

    @Test
    @DisplayName("Should produce different salts for the same password")
    void testSaltGenerationProducesDifferentValues() throws Exception {
        Password p1 = Password.of("test");
        Password p2 = Password.of("test");
        assertFalse(Arrays.equals(p1.salt, p2.salt));
    }

    // ==================== Verification Tests ====================

    @Test
    @DisplayName("Should verify the correct password")
    void testVerifyCorrectPassword() throws Exception {
        Password password = Password.of("myPassword");
        assertTrue(password.verify("myPassword"));
    }

    @Test
    @DisplayName("Should reject the wrong password")
    void testVerifyWrongPassword() throws Exception {
        Password password = Password.of("myPassword");
        assertFalse(password.verify("wrongPassword"));
    }

    @Test
    @DisplayName("Should verify correctly after reconstitution from stored hash and salt")
    void testReconstitutedPasswordVerifiesCorrectly() throws Exception {
        Password original = Password.of("myPassword");
        Password reconstituted = Password.reconstitute(original.hash, original.salt);
        assertTrue(reconstituted.verify("myPassword"));
    }
}