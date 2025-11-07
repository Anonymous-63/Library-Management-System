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

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final BookRepo bookRepo;
    private final AbacPolicyRepo abacPolicyRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        /*Privilege readPrivilege = Privilege.builder()
                .name("READ_PRIVILEGE")
                .enabled(true).build();
        Privilege writePrivilege = Privilege.builder()
                .name("WRITE_PRIVILEGE")
                .enabled(true).build();
        privilegeRepo.saveAll(Set.of(readPrivilege, writePrivilege));

        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .privileges(Set.of(readPrivilege, writePrivilege)).enabled(true).build();
        Role userRole = Role.builder()
                .name("ROLE_USER")
                .privileges(Set.of(readPrivilege)).enabled(true).build();
        roleRepo.saveAll(Set.of(adminRole, userRole));

        User admin = User.builder()
                .name("Admin User")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .roles(Set.of(adminRole))
                .status(AccountStatus.ACTIVE)
                .enabled(true)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .lastLogin(Instant.now())
                .build();
        User normalUser = User.builder()
                .name("Normal User")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .roles(Set.of(userRole))
                .status(AccountStatus.ACTIVE)
                .enabled(true)
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .lastLogin(Instant.now())
                .build();
        userRepo.saveAll(Set.of(admin, normalUser));

        Book book1 = Book.builder()
                .title("Spring Boot in Action")
                .author("Craig Walls")
                .publisher("Manning")
                .category("Programming")
                .status(BookStatus.AVAILABLE)
                .totalCopies(5)
                .availableCopies(5)
                .addedBy(admin)
                .description("A comprehensive guide to Spring Boot.")
                .enabled(true)
                .build();
        Book book2 = Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .publisher("Prentice Hall")
                .category("Programming")
                .status(BookStatus.AVAILABLE)
                .totalCopies(3)
                .availableCopies(3)
                .addedBy(admin)
                .description("A handbook of agile software craftsmanship.")
                .enabled(true)
                .build();
        bookRepo.saveAll(Set.of(book1, book2));
        System.out.println("Dummy data initialized successfully!");*/

        /*// ALLOW if role is ADMIN and doc is not archived
        AbacPolicy adminRead = new AbacPolicy();
        adminRead.setName("Admins can read active docs");
        adminRead.setDescription("Allow ADMIN role to read if doc is not archived");
        adminRead.setResourceType("document");
        adminRead.setAction("READ");
        adminRead.setEffect("ALLOW");
        adminRead.setConditions("#subject['roles'].contains('ADMIN') && !#resource['archived']");

        // ALLOW if owner matches subject username
        AbacPolicy ownerUpdate = new AbacPolicy();
        ownerUpdate.setName("Owner can update their doc");
        ownerUpdate.setDescription("Allow update if subject is owner of document");
        ownerUpdate.setResourceType("document");
        ownerUpdate.setAction("UPDATE");
        ownerUpdate.setEffect("ALLOW");
        ownerUpdate.setConditions("#subject['username'] == #resource['owner']");

        // DENY UPDATE if document is locked
        AbacPolicy lockedDenyUpdate = new AbacPolicy();
        lockedDenyUpdate.setName("Deny update on locked docs");
        lockedDenyUpdate.setDescription("Deny update on locked document");
        lockedDenyUpdate.setResourceType("document");
        lockedDenyUpdate.setAction("UPDATE");
        lockedDenyUpdate.setEffect("DENY");
        lockedDenyUpdate.setConditions("#resource['locked'] == true");

        abacPolicyRepo.save(adminRead);
        abacPolicyRepo.save(ownerUpdate);
        abacPolicyRepo.save(lockedDenyUpdate);*/
    }
}
