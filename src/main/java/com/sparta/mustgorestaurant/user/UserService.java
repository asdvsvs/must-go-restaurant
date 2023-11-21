package com.sparta.mustgorestaurant.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserRequestDto requestDto) {
        String username = requestDto.username;
        String password = passwordEncoder.encode(requestDto.password);

        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("중복된 사용자 이름이 있습니다.");
        }
        User user = new User(username,password);
        userRepository.save(user);
    }
}