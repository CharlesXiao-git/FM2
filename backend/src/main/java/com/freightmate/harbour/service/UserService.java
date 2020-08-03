package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDetailsService detailsService;

    UserService(@Autowired UserDetailsService detailsService){
        this.detailsService = detailsService;
    }
    public List<User> getChildren(String username) {
        //todo replace with id from token when JWT is updated
        return detailsService.getChildren(detailsService.loadUserByUsername(username).getId());
    }
}
