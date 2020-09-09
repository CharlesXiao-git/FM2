package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class UserClient extends BaseEntity<Long> {

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    long userId;

    @ManyToOne(targetEntity = UserCustomer.class, fetch = FetchType.LAZY)
    UserCustomer userCustomer;

}

