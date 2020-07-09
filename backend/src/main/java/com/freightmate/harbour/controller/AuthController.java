package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuthRequest;
import com.freightmate.harbour.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(path="/login", method = RequestMethod.POST)
    public ResponseEntity<String> createJWT(@Valid @RequestBody AuthRequest request){

        // try generate a token using the provided credentials
        String token = this.authService.generateToken(request);

        // if we don't get a token back, the credentials were invalid
        if(Objects.isNull(token)){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Could not log in with supplied credentials");
        }

        // assuming we made it this far we have a valid token, so lets login
        return ResponseEntity.ok(token);
    }
}
