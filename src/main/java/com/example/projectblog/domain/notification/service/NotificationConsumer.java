package com.example.projectblog.domain.notification.service;

import com.example.projectblog.domain.notification.entity.NotificationType;
import com.example.projectblog.util.kafka.event.CommentCreatedEvent;
import com.example.projectblog.util.kafka.event.LikeEvent;
import com.example.projectblog.util.kafka.event.PostCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "blog.post.created", groupId = "blog-group")
  public void handlePostCreated(String message) {
    try {
      PostCreatedEvent event = objectMapper.readValue(message, PostCreatedEvent.class);
      notificationService.saveNotification(
          event.getAuthorUsername(),
          "system",
          NotificationType.POST_CREATED,
          event.getPostId(),
          String.format("게시글 '%s'이 성공적으로 작성되었습니다.", event.getPostTitle())
      );
    } catch (Exception e) {
      log.error("[Kafka] handlePostCreated failed: {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "blog.comment.created", groupId = "blog-group")
  public void handleCommentCreated(String message) {
    try {
      CommentCreatedEvent event = objectMapper.readValue(message, CommentCreatedEvent.class);

      // 본인 게시글에 본인이 댓글 작성한 경우 알림 제외
      if (event.getCommenterUsername().equals(event.getPostAuthorUsername())) {
        return;
      }

      String preview = event.getCommentContent().length() > 30
          ? event.getCommentContent().substring(0, 30) + "..."
          : event.getCommentContent();

      notificationService.saveNotification(
          event.getPostAuthorUsername(),
          event.getCommenterUsername(),
          NotificationType.COMMENT_CREATED,
          event.getPostId(),
          String.format("%s님이 내 게시글에 댓글을 달았습니다: \"%s\"", event.getCommenterUsername(), preview)
      );
    } catch (Exception e) {
      log.error("[Kafka] handleCommentCreated failed: {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "blog.post.liked", groupId = "blog-group")
  public void handlePostLiked(String message) {
    try {
      LikeEvent event = objectMapper.readValue(message, LikeEvent.class);

      if (event.getLikerUsername().equals(event.getOwnerUsername())) {
        return;
      }

      notificationService.saveNotification(
          event.getOwnerUsername(),
          event.getLikerUsername(),
          NotificationType.POST_LIKED,
          event.getTargetId(),
          String.format("%s님이 내 게시글을 좋아합니다.", event.getLikerUsername())
      );
    } catch (Exception e) {
      log.error("[Kafka] handlePostLiked failed: {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "blog.comment.liked", groupId = "blog-group")
  public void handleCommentLiked(String message) {
    try {
      LikeEvent event = objectMapper.readValue(message, LikeEvent.class);

      if (event.getLikerUsername().equals(event.getOwnerUsername())) {
        return;
      }

      notificationService.saveNotification(
          event.getOwnerUsername(),
          event.getLikerUsername(),
          NotificationType.COMMENT_LIKED,
          event.getTargetId(),
          String.format("%s님이 내 댓글을 좋아합니다.", event.getLikerUsername())
      );
    } catch (Exception e) {
      log.error("[Kafka] handleCommentLiked failed: {}", e.getMessage());
    }
  }
}
