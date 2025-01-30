package com.srt.SpringAuth.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthDto {
    private String accessToken;
    private String refreshToken;

    public String toString() {
        return String.format(
                "{ \"accessToken\": \"%s\", \"refreshToken\": \"%s\"",
                accessToken, refreshToken);
    }
}
