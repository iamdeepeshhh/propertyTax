package com.GAssociatesWeb.GAssociates.DTO.UserAccessDto;

import lombok.Data;

@Data
public class UserAccess_Dto {
    private Long id;
    private String username;
    private String password;
    private String profile;
    private String status;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
}
