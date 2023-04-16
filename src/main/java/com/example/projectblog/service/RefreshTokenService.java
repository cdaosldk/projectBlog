package com.example.projectblog.service;

import com.example.projectblog.entity.RefreshToken;
import com.example.projectblog.entity.User;
import com.example.projectblog.jwt.JwtUtil;
import com.example.projectblog.repository.RefreshTokenRepository;
import com.example.projectblog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final UserRepository userRepository;

  private final RefreshTokenRepository refreshTokenRepository;

  private final JwtUtil jwtUtil;

  public static final String REFRESH_HEADER = "Refresh";

  private static final String BEARER_PREFIX = "Bearer ";

  public String createRefreshToken(String username) {
    RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), findByUsername(username).getId());
    saveRefreshToken(refreshToken);
    return refreshToken.toString();
  }

  public void saveRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken getRefreshToken(HttpServletRequest request) {
    refreshTokenRepository.findByIdAndUserId(request.getHeader(REFRESH_HEADER)).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 리프레시 토큰입니다.")
    );
  }

    private User findByUsername (String username){
      return userRepository.findByUsername(username)
          .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다"));
    }
  }
}
