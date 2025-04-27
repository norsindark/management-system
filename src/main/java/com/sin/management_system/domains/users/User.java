package com.sin.management_system.domains.users;

import com.sin.management_system.domains.roles.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int MAX_LENGTH = 255;

    @Id
    @UuidGenerator
    @Column(name = "user_id", unique = true, length = 36, nullable = false)
    private String id;

    @Column(name = "email", nullable = false, unique = true, length = MAX_LENGTH)
    @Email
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = MAX_LENGTH)
    private String username;

    @Column(name = "full_name", nullable = false, length = MAX_LENGTH)
    private String fullName;

    @Column(name = "password", nullable = false, length = MAX_LENGTH)
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
