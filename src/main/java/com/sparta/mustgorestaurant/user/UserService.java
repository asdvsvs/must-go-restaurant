package com.sparta.mustgorestaurant.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("중복된 사용자 이름이 있습니다.");
        }
        User user = new User(username,password);
        userRepository.save(user);
    }

    public void login(UserRequestDto requestDto) {
        User user=userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(()->new IllegalArgumentException("입력한 이름은 존재하지 않습니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호 다름");
        }
    }

    public UserResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 id의 유저는 존재하지 않습니다."));
        return new UserResponseDto(user);
    }
}