package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserDetailsService detailsService;

    UserService(@Autowired UserDetailsService detailsService){
        this.detailsService = detailsService;
    }
    public List<User> getChildren(User user) {
        return this.getChildren(user, false);
    }

    public List<User> getChildren(String username) {
        return this.getChildren(detailsService.loadUserByUsername(username), false);
    }

    public List<User> getChildren(User user, Boolean selfIfNoChildren) {
        //todo replace with id from token when JWT is updated
        List<User> children = detailsService.getChildren(user.getId());
        if(children.isEmpty() && selfIfNoChildren) {
            return Collections.singletonList(user);
        }
        return children;
    }
}
