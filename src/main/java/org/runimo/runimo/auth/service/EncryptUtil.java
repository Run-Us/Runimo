package org.runimo.runimo.auth.service;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptUtil {

    @Value("${runimo.security.secret-key}")
    private String secretKey;
    @Value("${runimo.security.iv}")
    private String iv;
    private static final String CIPHER_TRANS = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted);
    }
}
