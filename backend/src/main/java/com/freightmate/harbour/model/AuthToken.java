package com.freightmate.harbour.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthToken {
    int id;
    String firstName;
    String lastName;
    UserRole role;
}
