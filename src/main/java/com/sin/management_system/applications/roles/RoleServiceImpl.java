package com.sin.management_system.applications.roles;

import com.sin.management_system.domains.roles.Role;
import com.sin.management_system.domains.roles.RoleName;
import com.sin.management_system.domains.roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getRoleDefault() {
        return roleRepository.findByRoleName(RoleName.CUSTOMER);
    }

    @Override
    public void saveAllRoles(List<Role> roles) {
        roleRepository.saveAll(roles);
    }
}
