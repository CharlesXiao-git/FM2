package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class User extends BaseEntity<Long> {
    @Column (nullable = false, unique = true, length = 100)
    String username;

    @Setter
    @Pattern(
            regexp = "\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}",
            message = "Password must be valid bcrypt string",
            flags = Pattern.Flag.UNICODE_CASE
    )
    @Column (nullable = false)
    String password;

    @Column (nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    Unit preferredUnit;
    Boolean isAdmin;
    String token;

    // If we are a client this should be defined.
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    UserClient userClient;

    // If we are a customer this should be defined.
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    UserCustomer userCustomer;

    // If we are a broker this should be defined.
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    UserBroker userBroker;

    public boolean isUserCustomer() {
        return Objects.nonNull(userCustomer);
    }

    public boolean isUserBroker() {
        return Objects.nonNull(userBroker);
    }

    public boolean isUserClient() { return Objects.nonNull(userClient); }

    public UserRole getUserRole(){
        if(this.isUserBroker()){
            return UserRole.BROKER;
        }

        if(this.isUserCustomer()){
            return UserRole.CUSTOMER;
        }

        return UserRole.CLIENT;
    }
}
