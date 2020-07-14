package com.freightmate.harbour.service;

import com.auth0.jwt.JWT;
import com.freightmate.harbour.model.AuthRequest;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.when;

public class AuthServiceTest {
    FreightmateUserDetailsService userServiceMock = Mockito.mock(FreightmateUserDetailsService.class);
    AuthService service = new AuthService("TEST", 3600, userServiceMock);

    @Before
    public void before() {
        when(userServiceMock.loadUserByUsername(Mockito.anyString()))
                .thenReturn(
                User.builder()
                .password("$2y$12$yzT9Xz59Z46bneHC8HFxcuX5AMAJ64oVf5ljghqZ4TirYLTuAYvFC") //test
                .username("TestUser")
                .email("test@test.com")
                .userRole(UserRole.CLIENT)
                .build()
        );
    }

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
        AuthService service_2 = new AuthService("WRONGSECRET", 3600,userServiceMock);

        String jwt = service_2.generateToken(
                new AuthRequest("TestUser", "TestPassword")
        );
        assert !this.service.isValidToken(jwt);
    }

    @Test
    public void AuthServiceShouldSucceed_WhenExistingUserProvidedWithRightPassword() {
        AuthRequest user = new AuthRequest("TestUser", "TestPassword");

        String jwt = this.service.generateToken(user);

        assert service.decodeToken(jwt).getClaim("sub").asString().equals(user.getUsername());

    }

}
