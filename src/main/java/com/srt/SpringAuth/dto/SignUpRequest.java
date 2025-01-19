package com.srt.SpringAuth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {
    @NotBlank(message = "username cannot be empty")
    @Size(min = 6, max = 255, message = "username must be between 6 and 255 characters long")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 6, max = 255, message = "password must be between 6 and 255 characters long")
    private String password;

    @NotBlank(message = "first name cannot be empty")
    @Size(max = 155, message = "first name must be up to 155 characters long")
    private String firstName;

    @NotBlank(message = "last name cannot be empty")
    @Size(max = 155, message = "last name must be up to 155 characters long")
    private String lastName; 
}
