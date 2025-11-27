package org.karunamay.core.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    private static final int iterationCount = 65536;
    private static final int keyLength = 128;
    private static final String algorithm = "PBKDF2WithHmacSHA256";

    private static String hashPassword(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return passwordFormat(
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash)
        );
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hash(String password) {
        try {
            return hashPassword(password, generateSalt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String storedPassword, String password) {
        try {
            String[] parts = storedPassword.split("\\$");
            String algorithm = parts[0];
            int iterationCount = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            String expectedHash = parts[3];

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String hashedRawPassword = Base64.getEncoder().encodeToString(hash);

            return expectedHash.equals(hashedRawPassword);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private static String passwordFormat(String salt, String hash) {
        return String.format("%s$%s$%s$%s", PasswordHasher.algorithm, PasswordHasher.iterationCount, salt, hash);
    }
}
