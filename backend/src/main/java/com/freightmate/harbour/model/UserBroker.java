package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserBroker extends BaseEntity<Long> {

    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    long userId;

    @OneToMany(
        targetEntity = UserCustomer.class,
        fetch = FetchType.LAZY,
        mappedBy = "userBroker"
    )
    List<UserCustomer> customers;

    String bookingsEmail;
    String serviceEmail;
}
