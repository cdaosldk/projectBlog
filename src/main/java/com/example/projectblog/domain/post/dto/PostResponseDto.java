package com.example.projectblog.domain.post.dto;

import com.example.projectblog.domain.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

  private Long id;
  private String title;
  private String contents;

  private int likePost;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  public PostResponseDto(Post post, int likePost) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.contents = post.getContents();
    this.likePost = likePost;
    this.createdAt = post.getCreatedAt();
    this.modifiedAt = post.getModifiedAt();
  }
}
