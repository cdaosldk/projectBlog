package com.example.projectblog.jwt;

import com.example.projectblog.dto.MessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j // 로깅에 대해 추상 레이어를 제공하는 인터페이스 라이브러리
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String resolveAccessToken = jwtUtil.resolveAccessToken(request);

    if (resolveAccessToken != null) {
      if (!jwtUtil.validateToken(resolveAccessToken, request, response)) {
        jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
      }
      Claims info = jwtUtil.getUserInfoFromToken(resolveAccessToken);
      setAuthenticaion(info.getSubject());
    }
    filterChain.doFilter(request, response);
  }

  public void setAuthenticaion(String username) { // 인증 객체 생성 및 등록 : 코드 간소화 및 책임 분리 목적
    SecurityContext context = SecurityContextHolder.createEmptyContext(); // ContextHolder에 인증 객체 저장
    Authentication authentication = jwtUtil.createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }

  // 토큰 오류가 발생한 경우, Exception 결과값을 사용자에게 반환한다
  public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
    response.setStatus(statusCode);
    response.setContentType("application/json");
    try {
      // ObjectMapper를 통해 변환한다
      String json = new ObjectMapper().writeValueAsString(new MessageResponseDto(msg, statusCode));
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
