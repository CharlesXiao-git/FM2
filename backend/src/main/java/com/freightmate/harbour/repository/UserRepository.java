package com.freightmate.harbour.repository;


import com.freightmate.harbour.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(@Param("username") String username);

}
