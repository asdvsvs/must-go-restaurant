package com.sparta.mustgorestaurant.user.dto;

import com.sparta.mustgorestaurant.CommonResponseDto;
import com.sparta.mustgorestaurant.user.User;
import lombok.Getter;

@Getter
public class UserResponseDto extends CommonResponseDto {
    private String username;
    private String introduction;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.introduction = user.getIntroduction();
    }
}
