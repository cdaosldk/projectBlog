package com.example.projectblog.util.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatedEvent {

  private Long postId;
  private String postTitle;
  private String authorUsername;
}
