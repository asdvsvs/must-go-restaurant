package com.sparta.mustgorestaurant.user;

import com.sparta.mustgorestaurant.post.Post;
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    private String introduction;

    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.introduction = "맛잘알";
    }
}
