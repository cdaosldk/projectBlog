package com.example.projectblog.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

  private String username;
  private String password;

  public LoginRequestDto(String username, String password) {
    this.username = username;
    this.password = password;
  }
}