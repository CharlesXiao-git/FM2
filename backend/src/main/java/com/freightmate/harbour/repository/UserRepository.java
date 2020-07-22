package com.freightmate.harbour.repository;


import com.freightmate.harbour.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    // todo cleanup the Repository/Userdetails/UserService layout. Currently confusing.
    String SELECT_USERS_BY_PARENT_QUERY = "from User u inner join u.customer c inner join u.broker b with ?1 in (c.id, b.id)";

    User findByUsername(@Param("username") String username);

    @Query(SELECT_USERS_BY_PARENT_QUERY)
    List<User> findUsersByParent(long userId);

}
