package com.example.projectblog.entity;

import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 336)
@Getter
public class RefreshToken {

  @Id
  private String refreshToken;

  private Long userId;

  public RefreshToken(String refreshToken, Long userId) {
    this.refreshToken = refreshToken;
    this.userId = userId;
  }

  public boolean checkRefreshToken(String token) {
    return refreshToken.equals(token);
  }
}
