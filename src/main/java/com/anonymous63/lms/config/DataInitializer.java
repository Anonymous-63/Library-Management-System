package com.anonymous63.lms.config;

import com.anonymous63.lms.entity.*;
import com.anonymous63.lms.enums.AccountStatus;
import com.anonymous63.lms.enums.BookStatus;
import com.anonymous63.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final BookRepo bookRepo;
    private final PolicyRepo policyRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Create privileges
        Privilege viewPrivilege = privilegeRepo.findByName("VIEW_BOOK")
                .orElseGet(() -> privilegeRepo.save(Privilege.builder().name("VIEW_BOOK").enabled(true).build()));
        Privilege managePrivilege = privilegeRepo.findByName("MANAGE_BOOK")
                .orElseGet(() -> privilegeRepo.save(Privilege.builder().name("MANAGE_BOOK").enabled(true).build()));

        // 2️⃣ Create roles
        Role adminRole = roleRepo.findByName("ADMIN")
                .orElseGet(() -> roleRepo.save(Role.builder()
                        .name("ADMIN")
                        .privileges(Set.of(viewPrivilege, managePrivilege))
                        .enabled(true)
                        .build()));

        Role userRole = roleRepo.findByName("USER")
                .orElseGet(() -> roleRepo.save(Role.builder()
                        .name("USER")
                        .privileges(Set.of(viewPrivilege))
                        .enabled(true)
                        .build()));

        // 3️⃣ Create users
        User admin = userRepo.findByEmail("admin@test.com")
                .orElseGet(() -> userRepo.save(User.builder()
                        .name("Admin User")
                        .email("admin@test.com")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(Set.of(adminRole))
                        .status(AccountStatus.ACTIVE)
                        .enabled(true)
                        .build()));

        User user = userRepo.findByEmail("user@test.com")
                .orElseGet(() -> userRepo.save(User.builder()
                        .name("Normal User")
                        .email("user@test.com")
                        .password(passwordEncoder.encode("user123"))
                        .roles(Set.of(userRole))
                        .status(AccountStatus.ACTIVE)
                        .enabled(true)
                        .build()));

        // 4️⃣ Create test books
        if (bookRepo.count() == 0) {
            Book book1 = Book.builder()
                    .title("Spring Boot in Action")
                    .author("Craig Walls")
                    .category("Programming")
                    .publisher("Manning")
                    .status(BookStatus.AVAILABLE)
                    .totalCopies(5)
                    .availableCopies(5)
                    .addedBy(admin)
                    .enabled(true)
                    .build();

            Book book2 = Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .category("Programming")
                    .publisher("Prentice Hall")
                    .status(BookStatus.AVAILABLE)
                    .totalCopies(3)
                    .availableCopies(3)
                    .addedBy(admin)
                    .enabled(true)
                    .build();

            bookRepo.saveAll(List.of(book1, book2));
        }

        // 5️⃣ Create test ABAC policies
        if (policyRepo.count() == 0) {
            Policy adminPolicy = Policy.builder()
                    .name("Admin Book Policy")
                    .resource("BOOK")
                    .conditionJson("""
                            {
                                "roles":["ADMIN"],
                                "actions":["CREATE","UPDATE","DELETE","VIEW"],
                                "attributes":{}
                            }
                            """)
                    .enabled(true)
                    .build();

            Policy userPolicy = Policy.builder()
                    .name("User Book Policy")
                    .resource("BOOK")
                    .conditionJson("""
                            {
                                "roles":["USER"],
                                "actions":["VIEW"],
                                "attributes":{}
                            }
                            """)
                    .enabled(true)
                    .build();

            policyRepo.saveAll(List.of(adminPolicy, userPolicy));
        }

    }
}
