package com.funeral.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class WXBizDataCrypt {
    private String appId;

    public WXBizDataCrypt(String appId) {
        this.appId = appId;
    }

    public String decryptData(String encryptedData, String sessionKey, String iv) throws Exception {
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        SecretKeySpec secretKey = new SecretKeySpec(sessionKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);
        return new String(decryptedBytes);
    }
}