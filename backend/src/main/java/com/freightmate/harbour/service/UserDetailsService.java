package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserLoginAttempt;
import com.freightmate.harbour.repository.UserLoginAttemptRepository;
import com.freightmate.harbour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginAttemptRepository userLoginAttemptRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserLoginAttempt createLoginAttempt(UserLoginAttempt loginAttempt) {
        return userLoginAttemptRepository.save(loginAttempt);
    }

    public Integer getLoginAttemptCountByIp(String requestIpAddress) {
        return userLoginAttemptRepository.getCountOfLoginAttemptByOriginIp(requestIpAddress);
    }

    public Integer getLoginAttemptCountByUsername(String username) {
        return userLoginAttemptRepository.getCountOfLoginAttemptByUsername(username);
    }

    public List<User> getChildren(long userId) {
        return userRepository.findUsersByParent(userId);
    }
}
