package com.anonymous63.lms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {
    private String username;
    private String name;
    private String email;
    private String phoneNo;
}
