package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

// todo: Need to find another way instead of using @Data
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username cannot be null or blank")
    String username;
    @NotBlank(message = "Password cannot be null or blank")
    String password;
    String requestIpAddress;
}
