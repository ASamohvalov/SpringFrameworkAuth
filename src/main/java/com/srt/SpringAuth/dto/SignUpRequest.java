package com.srt.SpringAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 6, max = 255)
    private String username;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @NotBlank
    @Size(max = 155)
    private String firstName;

    @NotBlank
    @Size(max = 155)
    private String lastName; 
}
