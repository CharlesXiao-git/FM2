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

    public List<User> getChildren(long userId) {
        return this.getChildren(userId, false);
    }

    public List<User> getChildren(long userId, Boolean selfIfNoChildren) {
        List<User> children = detailsService.getChildren(userId);
        if(children.isEmpty() && selfIfNoChildren) {
            return detailsService.getUsers(Collections.singletonList(userId));
        }
        return children;
    }

    public boolean isChildOf(long parentUserId, long childUserId) {
        return this
                .getChildren(parentUserId, true)
                .stream()
                .anyMatch(child -> child.getId() == childUserId);
    }
}
