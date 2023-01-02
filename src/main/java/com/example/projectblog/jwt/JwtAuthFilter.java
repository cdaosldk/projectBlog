package com.example.projectblog.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j // 로깅에 대해 추상 레이어를 제공하는 인터페이스 라이브러리
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // resolveToken를 통해 jwt 토큰방식으로 재구성된 사용자 정보를 지역변수 token에 할당
        String token = jwtUtil.resolveToken(request);

        // 1. token이 null값인지 체크 2. 유효한 토큰인지 체크 3. Claims 타입을 활용한 토큰 정보를 가져오기
        // 4. 사용자 정보 중 이름을 가져와 serAuthentication 메서드에 인자로 보냄 ~ 인증객체 생성
        if(token != null) {
            if(!jwtUtil.validateToken(token)) {
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthenticaion(info.getSubject());
        }
        filterChain.doFilter(request,response); // doFilter 메서드를 구현
        // 실제 필요한 필터를 생성하는 클래스 HttpSecurity
    }

    public void setAuthenticaion(String username) { // 인증 객체 생성 및 등록 : 코드 간소화 및 책임 분리 목적
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // ContextHolder에 인증 객체 저장
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}
