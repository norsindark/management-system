package com.sin.management_system.domains.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MAX_LENGTH = 255;

    @Id
    @UuidGenerator
    @Column(name = "role_id", nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "role_name", unique = true, length = MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
