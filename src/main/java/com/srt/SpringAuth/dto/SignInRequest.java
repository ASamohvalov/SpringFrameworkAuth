package com.srt.SpringAuth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInRequest {
    @NotBlank(message = "username cannot be empty")
    private String username;
    @NotBlank(message = "username cannot be empty")
    private String password;
}
