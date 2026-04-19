package com.example.projectblog.util.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedEvent {

  private Long commentId;
  private String commentContent;
  private String commenterUsername;
  private Long postId;
  private String postAuthorUsername;
}
