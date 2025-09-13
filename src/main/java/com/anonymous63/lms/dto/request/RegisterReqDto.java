package com.anonymous63.lms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
