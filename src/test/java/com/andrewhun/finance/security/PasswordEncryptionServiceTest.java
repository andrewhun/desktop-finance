/*
* This file contains the PasswordEncryptionServiceTest class, which is responsible for
* testing that passwords are encrypted and authenticated properly.
 */

package com.andrewhun.finance.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import static org.junit.jupiter.api.Assertions.fail;
import com.andrewhun.finance.security.PasswordEncryptionService;
import static com.andrewhun.finance.util.NamedConstants.*;

public class PasswordEncryptionServiceTest {

    private PasswordEncryptionService passwordEncryptionService = new PasswordEncryptionService();

    @Test
    void testGeneratingSalt() {

        try {
            passwordEncryptionService.generateSalt();
        }
        catch (NoSuchAlgorithmException e) {

            fail("Problems with the algorithm used for generating salt");
        }
    }

    @Test
    void testEncryptingPasswords() {

        try {
            byte[] salt = passwordEncryptionService.generateSalt();
            passwordEncryptionService.getEncryptedPassword(PASSWORD, salt);
        }
        catch (NoSuchAlgorithmException e) {

            fail("Problems with either the encryption algorithm or the salt-generation algorithm");
        }
        catch (InvalidKeySpecException ie) {

            fail("Problems with the key spec used for encrypting passwords");
        }
    }

    @Test
    void testAuthentication() {

        try {
            byte[] salt = passwordEncryptionService.generateSalt();
            byte[] encryptedPassword = passwordEncryptionService.getEncryptedPassword(PASSWORD, salt);
            Assertions.assertTrue(passwordEncryptionService.authenticate(PASSWORD, encryptedPassword, salt));
            Assertions.assertFalse(passwordEncryptionService.authenticate(INCORRECT_INPUT, encryptedPassword, salt));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            fail("Problems with the encryption service. Unable to authenticate passwords.");
        }
    }
}
