package com.andrewhun.finance.user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class Password {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 8;

    final byte[] hash;
    final byte[] salt;

    private Password(byte[] hash, byte[] salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public static Password of(String rawPassword) throws GeneralSecurityException {
        byte[] salt = generateSalt();
        byte[] hash = hash(rawPassword, salt);
        return new Password(hash, salt);
    }

    private static byte[] generateSalt() throws GeneralSecurityException {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(salt);
        return salt;
    }

    private static byte[] hash(String password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        }
        finally {
            spec.clearPassword();
        }
    }

    static Password reconstitute(byte[] hash, byte[] salt) {
        return new Password(hash, salt);
    }

    public boolean verify(String attemptedPassword) throws GeneralSecurityException {
        return MessageDigest.isEqual(hash, hash(attemptedPassword, salt));
    }
}