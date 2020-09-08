package com.freightmate.harbour.service;

import com.freightmate.harbour.exception.UserNotFoundException;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserCustomer;
import com.freightmate.harbour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDetailsService detailsService;
    private final UserRepository userRepository;

    UserService(@Autowired UserDetailsService detailsService,
                @Autowired UserRepository userRepository) {
        this.detailsService = detailsService;
        this.userRepository = userRepository;
    }

    public List<User> getChildren(long userId) {
        return this.getChildren(userId, false);
    }

    public List<User> getChildren(long userId, Boolean selfIfNoChildren) {
        Optional<User> user = userRepository.findUsersByIds(
                Collections.singletonList(userId)
        ).stream().findFirst();
        if (user.isEmpty()) {
            throw new UserNotFoundException("User cannot be found");
        }

        List<User> children = detailsService.getChildren(user.get());
        if (children.isEmpty() && selfIfNoChildren) {
            return Collections.singletonList(user.get());
        }
        return children;
    }

    public boolean isChildOf(long parentUserId, long childUserId) {
        return this
                .getParents(childUserId, true)
                .stream()
                .anyMatch(parent -> parent.getId() == parentUserId);
    }


    public List<User> getParents(long userId, Boolean selfIncluded) {
        Optional<User> opUser = this.userRepository.findById(userId);

        if (opUser.isEmpty()) {
            throw new UserNotFoundException("Unable to find user: " + userId);
        }

        User user = opUser.get();

        if (user.isUserBroker()) {
            return selfIncluded ? Collections.singletonList(user) : Collections.emptyList();
        }

        List<User> parents = new ArrayList<>();

        if (selfIncluded) {
            parents.add(user);
        }

        if (user.isUserCustomer()) {
            // if I'm a customer, get my UserCustomer row, then my UserBroker parent
            parents.add(user.getUserCustomer().getUserBroker().getUser());
            return parents;
        }

        UserCustomer customer = user.getUserClient().getUserCustomer();
        parents.add(customer.getUser());
        parents.add(customer.getUserBroker().getUser());

        return parents;

    }
}
