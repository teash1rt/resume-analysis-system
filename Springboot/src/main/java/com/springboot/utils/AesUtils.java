package com.springboot.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String CHARSET_NAME = "UTF-8";

    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(CHARSET_NAME));
        return Base64.encodeBase64String(encryptedBytes);
    }

//    public static String decrypt(String encryptedText, String key) throws Exception {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), "AES");
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedText));
//        return new String(decryptedBytes, CHARSET_NAME);
//    }
}