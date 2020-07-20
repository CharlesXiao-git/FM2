package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final FreightmateUserDetailsService detailsService;

    UserService(@Autowired FreightmateUserDetailsService detailsService){
        this.detailsService = detailsService;
    }
    public List<User> getChildren(User reqestor) {
        return detailsService.getChildren(reqestor);
    }
}
