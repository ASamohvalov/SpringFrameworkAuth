package com.srt.SpringAuth.utils;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import io.micrometer.common.lang.NonNull;

public class PBKDF2PasswordEncoder {
    private final String ALGORITHM = "PBKDF2WithHmacSHA1";

    public String encode(@NonNull String password) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Password encode error", e);
        }
    }

    public boolean verify(@NonNull String password, @NonNull String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid format of saved password");
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] computedHash = factory.generateSecret(spec).getEncoded();

            return slowEquals(storedHash, computedHash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking password", e);
        }
    }

    private boolean slowEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
