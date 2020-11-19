package pl.lodz.p.it.inz.sgruda.multiStore.utils;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log
@NoArgsConstructor
public class HashMethod {

    public String hash(String toHash) {
        return sha256(toHash);
    }
    public String hash(long toHash) {
        return sha256(String.valueOf(toHash));
    }

    private String sha256(String toHash) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.severe(e.getClass().toString() + " " + e.getMessage());
        }
        byte[] hash = new byte[0];
        if (digest != null) {
            hash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
