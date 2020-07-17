package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.UserLoginAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserLoginAttemptRepository extends CrudRepository<UserLoginAttempt, Long> {

    // The following query has hard coded timer of 1 minute
    String IP_FAILED_LOGIN_ATTEMPTS = "SELECT count(*) FROM user_login_attempt a " +
        "WHERE a.origin_ip = ?1 " +
        "AND a.login_attempt_at BETWEEN " +
        "DATE_SUB(NOW(), INTERVAL 1 MINUTE) AND NOW()";

    // The following query has hard coded timer of 1 minute
    String USER_FAILED_LOGIN_ATTEMPTS = "SELECT count(*) FROM user_login_attempt a " +
        "WHERE a.username = ?1 " +
        "AND a.login_attempt_at BETWEEN " +
        "DATE_SUB(NOW(), INTERVAL 1 MINUTE) AND NOW()";

    @Query(value = IP_FAILED_LOGIN_ATTEMPTS,
            nativeQuery = true)
    int getCountOfLoginAttemptByOriginIp(String originIp);

    @Query(value = USER_FAILED_LOGIN_ATTEMPTS,
            nativeQuery = true)
    int getCountOfLoginAttemptByUsername(String username);
}
