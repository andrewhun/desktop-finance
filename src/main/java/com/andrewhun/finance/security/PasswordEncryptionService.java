/*
* This file contains the PasswordEncryptionService class, which is used to ensure
* that user passwords are stored in securely. It also performs authentication actions.
* Credit goes to Jerry Orr. You can find his very detailed description of how this
* class works at https://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
*
 */
package com.andrewhun.finance.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PasswordEncryptionService {

    public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
    throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Encrypt the clear-text password using the same salt that was used for the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

        // The authentication succeeds if the two hashes are the same
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    public byte[] getEncryptedPassword(String password, byte[] salt)
    throws NoSuchAlgorithmException, InvalidKeySpecException{

        // Slight change to the source, replacing SHA-1 with SHA-512, since it is more recent
        String ALGORITHM = "PBKDF2WithHmacSHA512";
        // A key length of 256 is supposedly safe
        int derivedKeyLength = 256;
        /* The iteration count determines how many times the algorithm is run, which in turn
        determines how slow the process is. In this case, the slower the better, as it will
        slow down brute-force attacks drastically, while having little to no impact on the
        experience of normal users. An iteration count of at least 1000 is recommended. */
        int iterations = 1000;

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

        return factory.generateSecret(keySpec).getEncoded();
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException {

        // VERY important to use SecureRandom instead of just Random
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        // Generate an 8 byte (64 bit) salt as recommended by RSA PKCS5
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        return salt;
    }
}
