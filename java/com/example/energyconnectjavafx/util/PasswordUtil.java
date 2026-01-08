package com.example.energyconnectjavafx.util;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class PasswordUtil {

    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String rawPassword, String storedHash) {

        if (storedHash == null) return false;

        if (storedHash.startsWith("$2a$") ||
                storedHash.startsWith("$2y$") ||
                storedHash.startsWith("$2b$")) {

            return false;
        }
        return hash(rawPassword).equals(storedHash);
    }
}