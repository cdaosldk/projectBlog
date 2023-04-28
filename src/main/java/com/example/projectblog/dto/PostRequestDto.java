package com.example.projectblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

  private String title;

  private String username;

  private String contents;

  public PostRequestDto(String title, String username, String contents) {
    this.title = title;
    this.username = username;
    this.contents = contents;
  }
}
