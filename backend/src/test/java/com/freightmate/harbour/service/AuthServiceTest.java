package com.freightmate.harbour.service;

import com.auth0.jwt.JWT;
import com.freightmate.harbour.model.AuthRequest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class AuthServiceTest {
    AuthService service = new AuthService("TEST", 3600);

    @Test
    public void AuthServiceShouldGenerateJWT_WithExpiryInTheFuture() {
        String jwt = this.service.generateToken(
                new AuthRequest("TestUser", "TestPassword")
        );
        assert JWT.decode(jwt).getExpiresAt().toInstant().isAfter(new Date(System.currentTimeMillis()).toInstant());
    }

    @Test
    public void AuthServiceShouldSucceedDecoding_WhenGivenCorrectSecret() {
        String user = "TestUser";

        String jwt = this.service.generateToken(
                new AuthRequest(user, "TestPassword")
        );
        assert service.isValidToken(jwt);
        assert service.decodeToken(jwt).getClaim("sub").asString().equals(user);
    }

    @Test
    public void AuthServiceShouldFail_WhenDecodingWithTheWrongSecret() {
        AuthService service_2 = new AuthService("WRONGSECRET", 3600);

        String jwt = service_2.generateToken(
                new AuthRequest("TestUser", "TestPassword")
        );
        assert !this.service.isValidToken(jwt);
    }

    @Test
    public void AuthServiceShouldSucceed_WhenExistingUserProvidedWithRightPassword() {
        AuthRequest user = new AuthRequest("slalom1", "Slalom01");

        String jwt = this.service.generateToken(user);

        assert service.decodeToken(jwt).getClaim("sub").asString().equals(user.getUsername());

    }

}
