package com.srt.SpringAuth.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayloadDto {
    private Long id;
    private String username;
    private long iat;
    private long exp; 
}
