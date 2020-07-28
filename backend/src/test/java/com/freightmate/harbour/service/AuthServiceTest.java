package com.freightmate.harbour.service;

import com.auth0.jwt.JWT;
import com.freightmate.harbour.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.when;

public class AuthServiceTest {
    UserDetailsService userServiceMock = Mockito.mock(UserDetailsService.class);
    AuthService service = new AuthService("TEST", 3600, userServiceMock);

    @Before
    public void before() {
        when(userServiceMock.loadUserByUsername(Mockito.anyString()))
                .thenReturn(
                User.builder()
                .password("$2a$10$759Q/obZ.3cKX2a4.Y4j8.SgmqnLGBVSIgzhr0sgFUBBD2FF04aqm") //TestPassword
                .username("TestUser")
                .email("test@test.com")
                .userRole(UserRole.CLIENT)
                .build()
        );
    }

    @Test
    public void AuthServiceShouldGenerateJWT_WithExpiryInTheFuture() {
        LoginResult loginResult = this.service.attemptLogin(
                new LoginRequest("TestUser", "TestPassword", "0.0.0.0")
        );
        assert JWT.decode(loginResult.getToken()).getExpiresAt().toInstant().isAfter(new Date(System.currentTimeMillis()).toInstant());
    }

    @Test
    public void AuthServiceShouldSucceedDecoding_WhenGivenCorrectSecret() {
        String user = "TestUser";

        LoginResult loginResult = this.service.attemptLogin(
                new LoginRequest(user, "TestPassword", "0.0.0.0")
        );
        assert service.isValidToken(loginResult.getToken());
        assert service.decodeToken(loginResult.getToken()).getClaim("sub").asString().equals(user);
    }

    @Test
    public void AuthServiceShouldFail_WhenDecodingWithTheWrongSecret() {
        AuthService service_2 = new AuthService("WRONGSECRET", 3600, userServiceMock);

        LoginResult loginResult = service_2.attemptLogin(
                new LoginRequest("TestUser", "TestPassword", "0.0.0.0")
        );
        assert !this.service.isValidToken(loginResult.getToken());
    }

    @Test
    public void AuthServiceShouldSucceed_WhenExistingUserProvidedWithRightPassword() {
        LoginRequest user = new LoginRequest("TestUser", "TestPassword", "0.0.0.0");

        LoginResult loginResult = this.service.attemptLogin(user);

        assert service.decodeToken(loginResult.getToken()).getClaim("sub").asString().equals(user.getUsername());

    }

    @Test
    public void AuthServiceShouldLockUser_WhenUserProvidedWrongUsernameFor5Times() {
        // Mock failed login attempt 5 times
        when(userServiceMock.getLoginAttemptCountByUsername(Mockito.anyString()))
                .thenReturn(5);

        LoginResult loginResult = this.service.attemptLogin(
                new LoginRequest("Test", "ANYPASSWORD", "127.0.0.1")
        );

        assert loginResult.getLoginResponse().equals(LoginResponse.LOCKED);
    }

    @Test
    public void AuthServiceShouldLockUser_WhenUserProvidedWrongPasswordFor5Times() {
        // Mock failed login attempt 5 times
        when(userServiceMock.getLoginAttemptCountByIp(Mockito.anyString()))
                .thenReturn(5);

        LoginResult loginResult = this.service.attemptLogin(
                new LoginRequest("TestUser", "Test", "127.0.0.1")
        );

        assert loginResult.getLoginResponse().equals(LoginResponse.LOCKED);
    }

    @Test
    public void AuthServiceShouldLockUser_WhenLockedUserProvidedCorrectPassword() {
        when(userServiceMock.getLoginAttemptCountByIp(Mockito.anyString()))
                .thenReturn(5);

        LoginResult loginResult1 = this.service.attemptLogin(
                new LoginRequest("TestUser", "Test", "127.0.0.1")
        );

        // Login with correct password after locked
        LoginResult loginResult2 = this.service.attemptLogin(
                new LoginRequest("TestUser", "TestPassword", "127.0.0.1")
        );

        assert loginResult2.getLoginResponse().equals(LoginResponse.LOCKED);
    }

    @Test
    public void AuthServiceShouldSucceed_WhenLoginAfterLockHasBeenReset() {
        when(userServiceMock.getLoginAttemptCountByIp(Mockito.anyString()))
                .thenReturn(5);

        LoginResult loginResult1 = this.service.attemptLogin(
                new LoginRequest("TestUser", "Test", "127.0.0.1")
        );

        // Mock the login attempt count to 0
        when(userServiceMock.getLoginAttemptCountByIp(Mockito.anyString()))
                .thenReturn(0);

        // Login with correct password after locked
        LoginResult loginResult2 = this.service.attemptLogin(
                new LoginRequest("TestUser", "TestPassword", "127.0.0.1")
        );

        assert this.service.isValidToken(loginResult2.getToken());
    }

}
