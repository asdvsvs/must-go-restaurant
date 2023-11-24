package com.sparta.mustgorestaurant.user;

import com.sparta.mustgorestaurant.CommonResponseDto;
import com.sparta.mustgorestaurant.jwt.JwtUtil;
import com.sparta.mustgorestaurant.user.dto.UserRequestDto;
import com.sparta.mustgorestaurant.user.dto.UserResponseDto;
import com.sparta.mustgorestaurant.user.userDetails.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(@RequestBody UserRequestDto requestDto){
        try {
            userService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody UserRequestDto requestDto, HttpServletResponse response){
        try {
            userService.login(requestDto);
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(requestDto.getUsername()));
            return ResponseEntity.ok().body(new CommonResponseDto("로그인 성공",HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponseDto> getUserInfo(@PathVariable Long id){
        try {
            return ResponseEntity.ok().body(userService.getUserInfo(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponseDto> updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody UserRequestDto requestDto){
        try {
            userService.update(userDetails.getUser(),id,requestDto);
            return ResponseEntity.ok().body(new CommonResponseDto("프로필 수정 완료!", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
