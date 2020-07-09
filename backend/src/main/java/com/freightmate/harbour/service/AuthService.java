package com.freightmate.harbour.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.freightmate.harbour.model.AuthRequest;
import com.freightmate.harbour.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class AuthService {
    private final int tokenExpiry;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    private FreightmateUserDetailsService freightmateUserDetailsService;

    AuthService(@Value("${jwt.secret}") String secret,
                @Value("${jwt.expiry}") int tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
        this.algorithm = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("FreightMate")
                .build();
    }

    public String generateToken(AuthRequest request) {
        User user = getUser(request.getUsername());

        if (!this.verifyPassword(request, user)) {
            return null;
        }
        return this.generateJWT(request, user);
    }

    public DecodedJWT decodeToken(String token) {
        return this.verifier.verify(token);
    }

    public boolean isValidToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    //todo: Check password against user password once user CRUD is created
    private boolean verifyPassword(AuthRequest request, User user) {
        // todo replace this with user.getPassword() once user CRUD is complete
        // String dbHashPlaceholder = "$2y$12$KwGiUF3awTTXtBJKdlC8Je.Y7vq4i1eVs7cPUoOBcXXvbYYcbmKAm";
        String dbHashPlaceholder = user.getPassword();

        // This is a remnant of the last system. Everything is SHA512'd then bcrypted.
        // This will revert the double hashing and then log you in
        if (this.bCryptEncoder.matches(generateSHA512Hash(request.getPassword()), dbHashPlaceholder)) {
            try {
                this.updatePassword(request.getUsername(), request.getPassword());
                return true;
            } catch (Exception e) {
                LOG.warn(String.format("Unable to revert doubled SHA512 hashing for user: %s", request.getUsername()));
            }
        }

        // if the user doesn't have a double hashed password, proceed normally
        return this.bCryptEncoder.matches(request.getPassword(), dbHashPlaceholder);
    }

    private boolean updatePassword(String username, String password) {
        // todo update the users password bcrypt their input
        return true;

    }

    //todo this a placeholder to get the user based on username this will likely be a userrepo.getByUsename()
    private User getUser(String username) {
        return this.freightmateUserDetailsService.loadUserByUsername(username);
    }

    //generate token for user
    private String generateJWT(AuthRequest authRequest, User user) {
        //todo add some relevant user details to the JWT
        return JWT.create()
                .withSubject(authRequest.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (tokenExpiry * 1000)))
                .withIssuer("FreightMate")
                .sign(algorithm);
    }

    private String generateSHA512Hash(String input) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return (generateSHA512Hash(input, salt));
    }

    private String generateSHA512Hash(String input, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unable to find the correct ago", e.getCause());
            throw new RuntimeException(e);
        }
    }
}
