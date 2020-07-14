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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;

@Component
public class AuthService {
    private final int tokenExpiry;
    private final Algorithm algorithm;
    private final FreightmateUserDetailsService userDetailsService;
    private final JWTVerifier verifier;
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    AuthService(@Value("${jwt.secret}") String secret,
                @Value("${jwt.expiry}") int tokenExpiry,
                @Autowired FreightmateUserDetailsService userDetailsService) {
        this.tokenExpiry = tokenExpiry;
        this.algorithm = Algorithm.HMAC512(secret);
        this.userDetailsService = userDetailsService;
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("FreightMate")
                .build();
    }

    public String generateToken(AuthRequest request) {
        User user = this.userDetailsService
                .loadUserByUsername(request.getUsername());

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

        // Always run a comparison against a failing password if the user doesn't exist so we still take the same time
        // regardless of if the user exists or not. Harder to brute force if time is constant whether or not the user exists
        // For a valid bcrypt string it must have $2y$10$ followed by 53 chars
        String dummyPassword = "$2y$10$.THISW.ILLNOT.WORKASITISNoT.ABCRYPTHASH.NOBCRYPT.HERE";
        String existingHashedPassword = Objects.isNull(user) ? dummyPassword : user.getPassword();

        // This is a remnant of the last system. Everything is SHA512'd then bcrypted.
        // This will revert the double hashing and then log you in
        if (this.bCryptEncoder.matches(generateSHA512Hash(request.getPassword()), existingHashedPassword)) {
            try {
                this.updatePassword(user, request.getPassword());
                return true;
            } catch (Exception e) {
                LOG.error("Unable to update password.", e);
                LOG.warn(String.format("Unable to revert doubled SHA512 hashing for user: %s", request.getUsername()));
            }
        }

        // if the user doesn't have a double hashed password, proceed normally
        return this.bCryptEncoder.matches(request.getPassword(), existingHashedPassword);
    }

    private User updatePassword(User user, String password) {
        user.setPassword(this.bCryptEncoder.encode(password));
        return this.userDetailsService.saveUser(user);
    }

    //generate token for user
    private String generateJWT(AuthRequest authRequest, User user) {
        //todo add some relevant user details to the JWT
        return JWT.create()
                .withSubject(authRequest.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (tokenExpiry * 1000)))
                .withIssuer("FreightMate")
                .withClaim("userRole", user.getUserRole().toString())
                .withClaim("email", user.getEmail())
                .sign(algorithm);
    }

    private String generateSHA512Hash(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
