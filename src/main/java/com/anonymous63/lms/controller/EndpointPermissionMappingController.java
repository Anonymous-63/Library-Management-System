package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.EndpointPermissionMappingReqDto;
import com.anonymous63.lms.dto.response.EndpointPermissionMappingResDto;
import com.anonymous63.lms.service.EndpointPermissionMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endpointPermissionMapping")
@RequiredArgsConstructor
public class EndpointPermissionMappingController {

    private final EndpointPermissionMappingService endpointPermissionMappingService;

    @PostMapping
    public EndpointPermissionMappingResDto create(@RequestBody EndpointPermissionMappingReqDto dto) {
        return endpointPermissionMappingService.createEndpointMapping(dto);
    }
}
