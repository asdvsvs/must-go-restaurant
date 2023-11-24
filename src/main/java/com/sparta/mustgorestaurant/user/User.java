package com.sparta.mustgorestaurant.user;

import com.sparta.mustgorestaurant.post.Post;
import com.sparta.mustgorestaurant.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    private String introduction;
    private String nickname;
    private String gender;
    private Integer age;
    private String address;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    public User(UserRequestDto requestDto, String password) {
        this.username = requestDto.getUsername();
        this.password = password;
        this.introduction = requestDto.getIntroduction();
        this.nickname =requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.age = requestDto.getAge();
        this.address = requestDto.getAddress();
        this.email= requestDto.getEmail();
    }


    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
