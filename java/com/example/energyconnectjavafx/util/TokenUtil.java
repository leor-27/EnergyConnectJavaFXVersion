package com.example.energyconnectjavafx.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class TokenUtil {

    public static String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return bytesToHex(bytes);
    }

    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return bytesToHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
