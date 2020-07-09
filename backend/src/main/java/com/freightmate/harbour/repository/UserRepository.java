package com.freightmate.harbour.repository;


import com.freightmate.harbour.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(@Param("lastname") String lastName);

    List<User> findByFirstName(@Param("firstname") String firstName);

    User findByUsername(@Param("username") String username);

}
