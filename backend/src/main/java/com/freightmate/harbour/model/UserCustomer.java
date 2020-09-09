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
public class UserCustomer extends BaseEntity<Long> {

    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    long userId;

    @ManyToOne(targetEntity = UserBroker.class,fetch = FetchType.LAZY)
    UserBroker userBroker;

    @OneToMany(
            targetEntity = UserClient.class,
            fetch = FetchType.LAZY,
            mappedBy = "userCustomer"
    )
    List<UserClient> clients;

    Boolean isManifestingActive;
    Boolean goodsPickup;
    Boolean status;
    Boolean sharedAddress;
    Boolean dispatchDateRollover;
    Boolean notificationEmail;
    Boolean service;
    Boolean consolidation;
    Boolean difot;
    Boolean pickupCutOff;
    Boolean extendedLabels;

}