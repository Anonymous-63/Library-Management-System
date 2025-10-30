package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.EndpointPermissionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EndpointPermissionMappingRepo extends JpaRepository<EndpointPermissionMapping, Long> {
    @Query("SELECT e.requiredPrivilege FROM EndpointPermissionMapping e " +
            "WHERE :path LIKE CONCAT(e.endpoint, '%') AND e.httpMethod = :method")
    String findPrivilegeByEndpointAndMethod(@Param("path") String path, @Param("method") String method);
}
