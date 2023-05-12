package com.example.projectblog.domain.user.service;

import com.example.projectblog.domain.user.entity.RefreshToken;
import com.example.projectblog.domain.user.repository.RefreshTokenRepository;
import com.example.projectblog.util.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  private static final String BEARER_PREFIX = "Bearer ";

  private static final long ACCESS_TOKEN_TIME = (long) 60 * 60;

  @Value("${jwt.secret.key}")
  private String secretKey;

  private Key key;

  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  private void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createRefreshToken(String username) {
    RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), username);
    saveRefreshToken(refreshToken);
    return refreshToken.getId() + ":" + refreshToken.getUsername();
  }

  public Cookie createRefreshCookie(String stringRefreshToken) {
    Cookie cookie = new Cookie("refreshToken", stringRefreshToken);
    cookie.setMaxAge(60 * 60 * 336);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    return cookie;
  }

  private void saveRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  public void checkRefreshToken(String StringRefreshToken, String username,
      HttpServletResponse response) {
    if (refreshTokenRepository.existsById(StringRefreshToken)) {
      regenerateRefreshToken(username, response);

    } else {
      RefreshToken refreshToken = refreshTokenRepository.findById(StringRefreshToken).orElseThrow(
          () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
      );
      deleteRefreshToken(refreshToken);
    }
  }

  public void deleteRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.delete(refreshToken);
  }

  private void regenerateRefreshToken(String username, HttpServletResponse response) {
    Date date = new Date();

    String newAccessToken = BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
  }

  public RefreshToken findById(String refreshToken) {
    return refreshTokenRepository.findById(refreshToken).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
    );
  }
}
