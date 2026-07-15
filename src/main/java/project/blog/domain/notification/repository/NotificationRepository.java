package project.blog.domain.notification.repository;

import project.blog.domain.notification.entity.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByReceiverUsernameOrderByCreatedAtDesc(String receiverUsername);

  long countByReceiverUsernameAndRead(String receiverUsername, boolean read);

  Optional<Notification> findByIdAndReceiverUsername(Long id, String receiverUsername);
}
