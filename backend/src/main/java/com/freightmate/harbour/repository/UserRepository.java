package com.freightmate.harbour.repository;


import com.freightmate.harbour.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    // todo cleanup the Repository/Userdetails/UserService layout. Currently confusing.

    String SELECT_USERS_BY_ID = "SELECT u.id, null AS password, u.username, u.email, u.is_admin, " +
            "       u.preferred_unit, u.token, u.is_deleted, u.deleted_at, u.created_at, u.updated_at, u.deleted_by, u.created_by, " +
            "       u.updated_by, u.original_id " +
            "FROM user u " +
            "WHERE u.id IN ?1";

    User findByUsername(@Param("username") String username);

    @Query(value = SELECT_USERS_BY_ID, nativeQuery = true)
    List<User> findUsersByIds(List<Long> userIds);
}
