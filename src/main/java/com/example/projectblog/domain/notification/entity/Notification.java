package com.example.projectblog.domain.notification.entity;

import com.example.projectblog.Timestamped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "notification")
public class Notification extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String receiverUsername;

  @Column(nullable = false)
  private String senderUsername;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  private Long referenceId;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private boolean read = false;

  public Notification(String receiverUsername, String senderUsername,
      NotificationType type, Long referenceId, String message) {
    this.receiverUsername = receiverUsername;
    this.senderUsername = senderUsername;
    this.type = type;
    this.referenceId = referenceId;
    this.message = message;
  }

  public void markAsRead() {
    this.read = true;
  }
}
