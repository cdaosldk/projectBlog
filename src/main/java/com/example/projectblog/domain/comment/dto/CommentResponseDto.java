package com.example.projectblog.domain.comment.dto;

import com.example.projectblog.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

  private Long id;

  private String username;

  private String comment;

  private int likeComment;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  public CommentResponseDto(Comment comment, int likeComment) {
    this.id = comment.getId();
    this.username = comment.getUsername();
    this.comment = comment.getComment();
    this.likeComment = likeComment;
    this.createdAt = comment.getCreatedAt();
    this.modifiedAt = comment.getModifiedAt();
  }
}
