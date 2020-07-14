package com.freightmate.harbour.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/example")
public class ExampleAuthController {

    @RequestMapping(path="/client", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('CLIENT','CUSTOMER','BROKER')")
    public ResponseEntity<String> client(HttpServletRequest request){
        // assuming we made it this far we have a valid token, so lets login
        return ResponseEntity.ok("You are at least a client");
    }

    @RequestMapping(path="/broker", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('BROKER')")
    public ResponseEntity<String> broker(HttpServletRequest request){
        // assuming we made it this far we have a valid token, so lets login
        return ResponseEntity.ok("You are indeed a Broker");
    }
}
