package com.freightmate.harbour.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
// todo: Might need to rethink if we should split User entity and UserDetails
public class User implements UserDetails {
    // PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String firstName;

    String lastName;

    @Column (name = "username", nullable = false, unique = true)
    String username;

    @Column (name = "password", nullable = false)
    String password;

    Date dateCreated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
