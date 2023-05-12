package com.example.projectblog.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

  private String username;
  private String comment;

  public CommentRequestDto(String username, String comment) {
    this.username = username;
    this.comment = comment;
  }
}
