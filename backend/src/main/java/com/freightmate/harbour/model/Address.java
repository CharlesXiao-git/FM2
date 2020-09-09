package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Address extends BaseEntity<Long> {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    // If we are a client this should be defined.
    @ManyToOne(targetEntity = UserClient.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_client_id")
    private UserClient userClient;

    // If we are a customer this should be defined.
    @ManyToOne(targetEntity = UserCustomer.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_customer_id")
    private UserCustomer userCustomer;

    // If we are a broker this should be defined.
    @ManyToOne(targetEntity = UserBroker.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_broker_id")
    private UserBroker userBroker;

    @ManyToOne(targetEntity = Suburb.class, fetch = FetchType.EAGER)
    private Suburb suburb;

    @Column(name = "user_customer_id", insertable = false, updatable = false)
    private Long userCustomerId;

    @Column(name = "user_client_id", insertable = false, updatable = false)
    private Long userClientId;

    @Column(name = "user_broker_id", insertable = false, updatable = false)
    private Long userBrokerId;

    private String referenceId;

    @Column(nullable = false)
    private String company;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(nullable = false)
    private String contactName;

    private String phoneNumber;

    private String email;

    private String specialInstructions;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean isDefault;

    public Address() {
        this.isDefault = false;
        this.isDeleted = false;
    }
}
