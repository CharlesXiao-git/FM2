package com.freightmate.harbour.repository;


import com.freightmate.harbour.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    // todo cleanup the Repository/Userdetails/UserService layout. Currently confusing.
    String SELECT_USERS_BY_PARENT_QUERY = "from User u inner join u.customer c inner join u.broker b with ?1 in (c.id, b.id)";

    String SELECT_USERS_BY_ID = "SELECT u.id, null AS password, u.username, u.email, u.broker_service_email, u.is_manifesting_active, u.user_role, u.broker_id, u.customer_id, " +
            "       u.preferred_unit, u.token, u.token_created_at, u.is_deleted, u.deleted_at, u.created_at, u.updated_at, u.deleted_by, u.created_by, " +
            "       u.updated_by, u.original_id, u.original_user_type " +
            "FROM user u " +
            "WHERE u.id IN ?1";

    User findByUsername(@Param("username") String username);

    @Query(SELECT_USERS_BY_PARENT_QUERY)
    List<User> findUsersByParent(long userId);

    @Query(value = SELECT_USERS_BY_ID, nativeQuery = true)
    List<User> findUsersByIds(List<Long> userIds);
}
