package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuthToken;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

        @RequestMapping(path="/children", method = RequestMethod.GET)
        public ResponseEntity<Map<String, Long>> getChildrenIdentifiers(Authentication authentication){
            long reqestorUserId = ((AuthToken) authentication.getPrincipal()).getUserId();

            List<User> children = userService.getChildren(reqestorUserId);

            if (children.isEmpty()){
                ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
            // Stream (loop) over the returned users, and construct a Map<username,userId> to return
            return ResponseEntity.ok(
                    children.stream()
                        .collect(
                                Collectors.toMap(
                                        User::getUsername,
                                        User::getId
                                )
                        )
                    );
        }
}
