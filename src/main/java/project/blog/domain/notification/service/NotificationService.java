package project.blog.domain.notification.service;

import project.blog.domain.notification.dto.NotificationResponseDto;
import project.blog.domain.notification.entity.Notification;
import project.blog.domain.notification.entity.NotificationType;
import project.blog.domain.notification.repository.NotificationRepository;
import project.common.dto.MessageResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Transactional
  public void saveNotification(String receiverUsername, String senderUsername,
      NotificationType type, Long referenceId, String message) {
    notificationRepository.save(
        new Notification(receiverUsername, senderUsername, type, referenceId, message));
  }

  @Transactional(readOnly = true)
  public List<NotificationResponseDto> getMyNotifications(String username) {
    return notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username)
        .stream()
        .map(NotificationResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public MessageResponseDto markAsRead(Long notificationId, String username) {
    Notification notification = notificationRepository
        .findByIdAndReceiverUsername(notificationId, username)
        .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
    notification.markAsRead();
    return new MessageResponseDto("읽음 처리 완료", HttpStatus.OK.value());
  }

  @Transactional(readOnly = true)
  public long getUnreadCount(String username) {
    return notificationRepository.countByReceiverUsernameAndRead(username, false);
  }
}
