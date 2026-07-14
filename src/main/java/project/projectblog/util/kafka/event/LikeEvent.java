package project.projectblog.util.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {

  private String targetType; // "POST" or "COMMENT"
  private Long targetId;
  private String likerUsername;
  private String ownerUsername;
}
