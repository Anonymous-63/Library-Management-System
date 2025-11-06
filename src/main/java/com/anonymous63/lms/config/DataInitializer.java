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
    private final AbacPolicyRepo abacPolicyRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // ALLOW if role is ADMIN and doc is not archived
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
        abacPolicyRepo.save(lockedDenyUpdate);
    }
}
