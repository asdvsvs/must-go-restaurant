package com.sparta.mustgorestaurant.jwt;

import java.io.IOException;
import java.util.Objects;

import com.sparta.mustgorestaurant.CommonResponseDto;
import com.sparta.mustgorestaurant.user.userDetails.UserDetailsImpl;
import com.sparta.mustgorestaurant.user.userDetails.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        if(!jwtUtil.isPresentToken(request)){
//            setResponse("토큰이 존재하지 않습니다.", response);
//            return;
//        }
        String token = jwtUtil.resolveToken(request);

        if(Objects.nonNull(token)) {
            if(jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);

                // 인증정보에 요저정보(username) 넣기
                // username -> user 조회
                String username = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // -> userDetails 에 담고
                UserDetailsImpl userDetails = userDetailsService.getUserDetails(username);
                // -> authentication의 principal 에 담고
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // -> securityContent 에 담고
                context.setAuthentication(authentication);
                // -> SecurityContextHolder 에 담고
                SecurityContextHolder.setContext(context);
                // -> 이제 @AuthenticationPrincipal 로 조회할 수 있음
            } else {
                // 인증정보가 존재하지 않을때
                setResponse("토큰이 유효하지 않습니다.", response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setResponse(String msg, HttpServletResponse response) throws IOException {
        CommonResponseDto commonResponseDto = new CommonResponseDto(msg, HttpStatus.BAD_REQUEST.value());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto));
    }
}