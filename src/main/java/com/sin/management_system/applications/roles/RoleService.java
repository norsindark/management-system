package com.sin.management_system.applications.roles;

import com.sin.management_system.domains.roles.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> getRoleDefault();

    void saveAllRoles(List<Role> roles);
}
