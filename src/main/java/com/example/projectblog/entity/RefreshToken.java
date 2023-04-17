package com.example.projectblog.entity;

import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 336L)
public class RefreshToken {

  @Id
  private String id;

  private String username;

  public RefreshToken(String id, String username) {
    this.id = id;
    this.username = username;
  }
}
