package com.anonymous63.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phoneNo;
}
