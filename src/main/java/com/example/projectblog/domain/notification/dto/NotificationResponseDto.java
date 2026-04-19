package com.example.projectblog.domain.notification.dto;

import com.example.projectblog.domain.notification.entity.Notification;
import com.example.projectblog.domain.notification.entity.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponseDto {

  private final Long id;
  private final String senderUsername;
  private final NotificationType type;
  private final Long referenceId;
  private final String message;
  private final boolean read;
  private final LocalDateTime createdAt;

  public NotificationResponseDto(Notification notification) {
    this.id = notification.getId();
    this.senderUsername = notification.getSenderUsername();
    this.type = notification.getType();
    this.referenceId = notification.getReferenceId();
    this.message = notification.getMessage();
    this.read = notification.isRead();
    this.createdAt = notification.getCreatedAt();
  }
}
