package com.freightmate.harbour.controller;

import com.freightmate.harbour.helper.RequestHelper;
import com.freightmate.harbour.model.LoginRequest;
import com.freightmate.harbour.model.LoginResponse;
import com.freightmate.harbour.model.LoginResult;
import com.freightmate.harbour.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * This is the login controller to validate the user's credential
     * @param request           The JSON request body that will be passed into the controller that contains the user's
     *                          credentials
     * @param servletRequest    This is required to retrieve the IP address of the user to log the attempt. Not required
     *                          to be passed in via the API
     * @return This controller will return a JWT if the credential is successfully validated or it will return an error
     * message
     */
    @RequestMapping(path="/login", method = RequestMethod.POST)
    public ResponseEntity<String> createJWT(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest){

        request.setRequestIpAddress(
                RequestHelper.extractRequestIp(servletRequest)
        );

        // try generate a token using the provided credentials
        LoginResult result = this.authService.attemptLogin(request);
        // if we don't get a token back, the credentials were invalid
        if(result.getLoginResponse().equals(LoginResponse.WRONG_USER_PASSWORD)){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Could not log in with supplied credentials");
        }

        if(result.getLoginResponse().equals(LoginResponse.LOCKED)){
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("User is locked. Please try again in 1 minute.");
        }

        // assuming we made it this far we have a valid token, so lets login
        return ResponseEntity.ok(result.getToken());
    }
}
