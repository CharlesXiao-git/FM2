package com.freightmate.harbour.service;

import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserClient;
import com.freightmate.harbour.model.UserLoginAttempt;
import com.freightmate.harbour.repository.UserLoginAttemptRepository;
import com.freightmate.harbour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Integer getFailedLoginAttemptCountByIp(String requestIpAddress) {
        return userLoginAttemptRepository.getCountOfFailedLoginAttemptByOriginIp(requestIpAddress);
    }

    public Integer getLoginAttemptCountByUsername(String username) {
        return userLoginAttemptRepository.getCountOfLoginAttemptByUsername(username);
    }

    // todo: Need to fix this because it is currently going to perform multiple select statements for every child which can take some time
    public List<User> getChildren(User user) {
        // todo: this might be a good candidate to cache
        if (user.isUserBroker()) {
            return user
                    .getUserBroker()
                    .getCustomers()
                    .stream()
                    .flatMap(userCustomer -> Stream
                            .concat(
                                    userCustomer
                                            .getClients()
                                            .stream()
                                            .map(UserClient::getUser),
                                    Stream.of(userCustomer.getUser())
                            )
                    )
                    .collect(Collectors.toList());
        }

        if (user.isUserCustomer()) {
            return user.getUserCustomer()
                    .getClients()
                    .stream()
                    .map(UserClient::getUser)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<User> getUsers(List<Long> userIds) {
        return userRepository.findUsersByIds(userIds);
    }
}
