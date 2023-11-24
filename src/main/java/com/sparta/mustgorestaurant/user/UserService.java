package com.sparta.mustgorestaurant.user;

import com.sparta.mustgorestaurant.user.dto.UserRequestDto;
import com.sparta.mustgorestaurant.user.dto.UserResponseDto;
import com.sparta.mustgorestaurant.user.userDetails.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이름이 있습니다.");
        }
        User user = new User(requestDto, password);
        userRepository.save(user);
    }

    public void login(UserRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("입력한 이름은 존재하지 않습니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 다름");
        }
    }

    public UserResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 유저는 존재하지 않습니다."));
        return new UserResponseDto(user);
    }


    @Transactional
    public void update(User user, Long id, UserRequestDto requestDto) {
        User user2 = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 유저는 존재하지 않습니다."));
        if (!Objects.equals(user.getId(), user2.getId())){
            throw new IllegalArgumentException("자신의 프로필만 수정 가능합니다.");
        }
        if (requestDto.getUsername()!=null) {
            user.updateUsername(requestDto.getUsername());
        }
        if (requestDto.getIntroduction()!=null) {
            user.updateIntroduction(requestDto.getIntroduction());
        }
        if (requestDto.getNewPassword()!=null) {
            if (requestDto.getPassword().isEmpty())
                throw new IllegalArgumentException("기존 비밀번호 입력이 필요합니다.");
            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
                throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
            user.updatePassword(requestDto.getNewPassword());
        }
    }
}