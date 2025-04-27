package com.sin.management_system.infrastructures.utils;

import com.sin.management_system.applications.roles.RoleService;
import com.sin.management_system.domains.roles.Role;
import com.sin.management_system.domains.roles.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.getRoleDefault().isEmpty()) {
            Role adminRole = Role.builder().roleName(RoleName.ADMIN).build();
            Role managerRole = Role.builder().roleName(RoleName.MANAGER).build();
            Role customerRole = Role.builder().roleName(RoleName.CUSTOMER).build();

            roleService.saveAllRoles(Arrays.asList(adminRole,managerRole,customerRole));
            System.out.println("✅ Roles created.");
        }
    }
}
