package com.graphicauth.authservice.service;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class AESCipherService {
    private AESCipherService(){}
    private static SecretKeySpec secretKey;
    private static final String SECRET ="T31yYJGgKfDyzhYlNFo3cA==";

    public static void setKey(String myKey)
    {
        MessageDigest sha;
        byte[] key;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String encrypt(String plainText)
    {
        try
        {
            setKey(SECRET);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            log.error("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String cipherText)
    {
        try
        {
            setKey(SECRET);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        }
        catch (Exception e)
        {
            log.error("Error while decrypting: " + e.toString());
        }
        return null;
    }

}
