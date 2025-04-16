package org.runimo.runimo.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptUtil {

    private static final String CIPHER_TRANS = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 32;
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH_BIT = 128;
    private final SecureRandom random = new SecureRandom();

    @Value("${runimo.security.secret-key}")
    private String secretKey;

    private SecretKeySpec createKeySpec() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] validKey = new byte[KEY_SIZE];

        System.arraycopy(keyBytes, 0, validKey, 0, Math.min(keyBytes.length, validKey.length));

        return new SecretKeySpec(validKey, ALGORITHM);
    }

    public String encrypt(String plainText) {
        try {
            byte[] ivBytes = new byte[IV_SIZE];
            this.random.nextBytes(ivBytes);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivBytes);

            Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
            cipher.init(Cipher.ENCRYPT_MODE, createKeySpec(), gcmParameterSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[ivBytes.length + encrypted.length];
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
            System.arraycopy(encrypted, 0, combined, ivBytes.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] combined = Base64.getDecoder().decode(cipherText);

            byte[] ivBytes = new byte[IV_SIZE];
            System.arraycopy(combined, 0, ivBytes, 0, ivBytes.length);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivBytes);

            byte[] encryptedBytes = new byte[combined.length - IV_SIZE];
            System.arraycopy(combined, IV_SIZE, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
            cipher.init(Cipher.DECRYPT_MODE, createKeySpec(), gcmParameterSpec);
            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}