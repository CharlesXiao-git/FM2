package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Username cannot be null or blank")
    String username;
    @NotBlank(message = "Password cannot be null or blank")
    String password;
}
