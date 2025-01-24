package com.srt.SpringAuth.services;

import java.util.Base64;

import com.srt.SpringAuth.models.User;
import com.srt.SpringAuth.utils.HmacSha256;

import lombok.Setter;

@Setter
public class AuthenticationService {
    // set from applicationContext
    private String secretKey;
    private HmacSha256 hmacSha256;

    public String authenticate(User user) {
        String decodedSecretKey = decodeSecretKey();

        String header = "{ \"alg\": \"HS256\", \"typ\": \"JWT\" }";
        String payload = String.format(
                "{ \"id\": %d, \"username\": \"%s\" }",
                user.getId(), user.getUsername());

        String encodedHeader = Base64.getEncoder().encodeToString(header.getBytes());
        String encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes());
        String unsignedToken = encodedHeader + "." + encodedPayload;

        String encodedSignature = hmacSha256.encode(decodedSecretKey, unsignedToken);
        return encodedHeader + "." + encodedPayload + "." + encodedSignature;
    }

    public boolean validateJwt(String jwt) {
        String decodedSecretKey = decodeSecretKey();
        String[] dividedJwt = jwt.split("[.]");
        return dividedJwt.length == 3 && 
                hmacSha256.encode(decodedSecretKey, dividedJwt[0] + "." + dividedJwt[1]).equals(dividedJwt[2]);
    }

    private String decodeSecretKey() {
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        return new String(decodedBytes);
    }
}
