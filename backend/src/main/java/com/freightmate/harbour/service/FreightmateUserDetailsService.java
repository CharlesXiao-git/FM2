package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import com.freightmate.harbour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FreightmateUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    //todo: need to implement DB connection and do a lookup of the username
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }
}
