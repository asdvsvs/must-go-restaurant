package com.sparta.mustgorestaurant.user;

import com.sparta.mustgorestaurant.CommonResponseDto;
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
