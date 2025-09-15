package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserResDto> save(@RequestBody UserReqDto reqDto) {
        UserResDto saveUser = userService.save(reqDto);
        return new ResponseEntity<>(saveUser, HttpStatus.OK);
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to Anonymous63 world;";
    }
}
