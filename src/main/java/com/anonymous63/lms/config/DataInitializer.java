package com.anonymous63.lms.config;

import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.PrivilegeRepo;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Create privileges if not exist
        Privilege addBook = createPrivilegeIfNotFound("ADD_BOOK");
        Privilege updateBook = createPrivilegeIfNotFound("UPDATE_BOOK");
        Privilege deleteBook = createPrivilegeIfNotFound("DELETE_BOOK");
        Privilege issueBook = createPrivilegeIfNotFound("ISSUE_BOOK");
        Privilege returnBook = createPrivilegeIfNotFound("RETURN_BOOK");
        Privilege viewBook = createPrivilegeIfNotFound("VIEW_BOOK");
        Privilege manageUsers = createPrivilegeIfNotFound("MANAGE_USERS");
        Privilege manageRoles = createPrivilegeIfNotFound("MANAGE_ROLES");
        Privilege manageFinance = createPrivilegeIfNotFound("MANAGE_FINANCE");

        // 2. Create roles with privileges if not exist
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN",
                Set.of(addBook, updateBook, deleteBook, issueBook, returnBook, viewBook, manageUsers, manageRoles, manageFinance));

        Role librarianRole = createRoleIfNotFound("ROLE_LIBRARIAN",
                Set.of(addBook, updateBook, deleteBook, issueBook, returnBook, viewBook));

        Role memberRole = createRoleIfNotFound("ROLE_MEMBER",
                Set.of(viewBook, issueBook, returnBook));

        Role accountantRole = createRoleIfNotFound("ROLE_ACCOUNTANT",
                Set.of(manageFinance));

        // 3. Create default admin user if not exist
        if (userRepo.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin")); // change in prod
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));
            userRepo.save(admin);
        }

    }

    private Privilege createPrivilegeIfNotFound(String name) {
        return privilegeRepo.findByName(name)
                .orElseGet(() -> privilegeRepo.save(new Privilege(null, name)));
    }

    private Role createRoleIfNotFound(String name, Set<Privilege> privileges) {
        return roleRepo.findByName(name)
                .orElseGet(() -> roleRepo.save(new Role(null, name, privileges)));
    }
}
