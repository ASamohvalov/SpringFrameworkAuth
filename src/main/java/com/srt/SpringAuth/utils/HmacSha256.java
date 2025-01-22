package com.srt.SpringAuth.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha256 {
    private final String ALGORITHM = "HmacSHA256";

    public String encode(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(ALGORITHM);

            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
            sha256_HMAC.init(secret_key);

            return byteArrayToHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
