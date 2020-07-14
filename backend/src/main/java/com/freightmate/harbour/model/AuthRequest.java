package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Username cannot be null or blank")
    String username;
    @NotBlank(message = "Password cannot be null or blank")
    String password;


}
