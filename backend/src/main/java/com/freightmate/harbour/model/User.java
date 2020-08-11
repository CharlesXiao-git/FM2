package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

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

    String brokerServiceEmail;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    User broker;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    User customer;

    @Column(nullable = false)
    Boolean isManifestingActive;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    @Enumerated(EnumType.STRING)
    Unit preferredUnit;

    @Column (length = 512)
    String token;
    LocalDateTime tokenCreatedAt;

    @Column(nullable = false)
    Boolean isDeleted;
    LocalDateTime deletedAt;
    Long deletedBy;
}
