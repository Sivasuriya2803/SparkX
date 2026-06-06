package com.revcast;

import com.revcast.common.audit.AuditLog;
import com.revcast.user.entity.Role;
import com.revcast.user.entity.User;
import com.revcast.user.repository.RoleRepository;
import com.revcast.user.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test Configuration for RevCast Tests
 */
@TestConfiguration
public class RevCastTestConfig {

    /**
     * Create test roles
     */
    @Bean
    public void createTestRoles(RoleRepository roleRepository) {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrator role")
                    .build();
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("PROJECT_MANAGER").isEmpty()) {
            Role pmRole = Role.builder()
                    .name("PROJECT_MANAGER")
                    .description("Project Manager role")
                    .build();
            roleRepository.save(pmRole);
        }

        if (roleRepository.findByName("DELIVERY_HEAD").isEmpty()) {
            Role deliveryRole = Role.builder()
                    .name("DELIVERY_HEAD")
                    .description("Delivery Head role")
                    .build();
            roleRepository.save(deliveryRole);
        }
    }

    /**
     * Create test user
     */
    @Bean
    public void createTestUser(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@revcast.com")
                    .password(passwordEncoder.encode("admin@123"))
                    .firstName("Admin")
                    .lastName("User")
                    .role(adminRole)
                    .isActive(true)
                    .build();
            userRepository.save(adminUser);
        }
    }
}

