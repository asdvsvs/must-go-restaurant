package com.sparta.mustgorestaurant.user.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String username;
    private String password;
    private String newPassword;
    private String introduction;
    private String nickname;
    private String gender;
    private Integer age;
    private String address;
    private String email;
}
