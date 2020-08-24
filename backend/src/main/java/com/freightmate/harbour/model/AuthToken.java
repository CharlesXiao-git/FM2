package com.freightmate.harbour.model;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Value;

import java.util.Map;

@Value
public class AuthToken {
    long userId;
    String username;
    String email;
    Unit preferredUnit;
    UserRole role;

    public AuthToken(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        this.userId = claims.get("id").asLong();
        this.username = decodedJWT.getSubject();
        this.email = claims.get("email").asString();
        this.preferredUnit = Unit.valueOf(claims.get("preferredUnit").asString());
        this.role = UserRole.valueOf(claims.get("userRole").asString());
    }
}
