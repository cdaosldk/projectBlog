package project.blog.domain.notification.entity;

import project.common.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
