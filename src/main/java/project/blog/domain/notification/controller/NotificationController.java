package project.blog.domain.notification.controller;

import project.blog.domain.notification.dto.NotificationResponseDto;
import project.blog.domain.notification.service.NotificationService;
import project.common.dto.MessageResponseDto;
import project.common.util.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(
        notificationService.getMyNotifications(userDetails.getUser().getUsername()));
  }

  @PutMapping("/{id}/read")
  public ResponseEntity<MessageResponseDto> markAsRead(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(
        notificationService.markAsRead(id, userDetails.getUser().getUsername()));
  }

  @GetMapping("/unread-count")
  public ResponseEntity<Long> getUnreadCount(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(
        notificationService.getUnreadCount(userDetails.getUser().getUsername()));
  }
}
