package com.srt.SpringAuth.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.srt.SpringAuth.dao.AuthDao;
import com.srt.SpringAuth.dto.jwt.JwtAuthDto;
import com.srt.SpringAuth.dto.jwt.JwtHeaderDto;
import com.srt.SpringAuth.dto.jwt.JwtPayloadDto;
import com.srt.SpringAuth.models.User;
import com.srt.SpringAuth.utils.HmacSha256;

import lombok.Setter;

@Setter
public class AuthenticationService {
    // set from applicationContext
    private String secretKey;
    private HmacSha256 hmacSha256;
    private JsonMapper jsonMapper;
    private AuthDao authDao;

    public JwtAuthDto authenticate(User user) {
        JwtAuthDto tokens = JwtAuthDto.builder()
                .accessToken(generateToken(user, Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond()))
                .refreshToken(generateToken(user, Instant.now().plus(60, ChronoUnit.DAYS).getEpochSecond()))
                .build();

        user.setToken(tokens.getRefreshToken());

        return tokens;
    }

    public boolean validateJwt(JwtAuthDto jwt) throws Exception {
        String[] dividedToken = jwt.getAccessToken().split("[.]");
        if (dividedToken.length != 3 &&
                !hmacSha256.encode(decodeSecretKey(), dividedToken[0] + "." + dividedToken[1])
                        .equals(dividedToken[2])) {
            return false;
        }

        JwtPayloadDto payload = jsonMapper.readValue(
                Base64.getDecoder().decode(dividedToken[1]),
                JwtPayloadDto.class);

        if (payload.getExp() <= Instant.now().getEpochSecond()) {
            String storedToken = authDao.getTokenById(payload.getId());
            if (!storedToken.equals(jwt.getRefreshToken())) {
                return false;
            }

            User user = User.builder()
                    .id(payload.getId())
                    .username(payload.getUsername())
                    .build();

            jwt.setAccessToken(generateToken(user, Instant.now().getEpochSecond()));
        }

        return true;
    }

    private String generateToken(User user, long exp) {
        try {
            String header = jsonMapper.writeValueAsString(
                    JwtHeaderDto.builder()
                            .alg("HS256")
                            .typ("JWT")
                            .build());
            String payload = jsonMapper.writeValueAsString(
                    JwtPayloadDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .exp(exp)
                            .iat(Instant.now().getEpochSecond())
                            .build());

            String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());
            String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());

            String encodedSignature = hmacSha256.encode(decodeSecretKey(), encodedHeader + "." + encodedPayload);

            return encodedHeader + "." + encodedPayload + "." + encodedSignature;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String decodeSecretKey() {
        byte[] decodedBytes = Base64.getDecoder().decode(secretKey);
        return new String(decodedBytes);
    }
}
