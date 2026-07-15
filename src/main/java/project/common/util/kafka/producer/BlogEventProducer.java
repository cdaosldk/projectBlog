package project.common.util.kafka.producer;

import project.common.util.kafka.event.CommentCreatedEvent;
import project.common.util.kafka.event.LikeEvent;
import project.common.util.kafka.event.PostCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogEventProducer {

  public static final String POST_CREATED = "blog.post.created";
  public static final String COMMENT_CREATED = "blog.comment.created";
  public static final String POST_LIKED = "blog.post.liked";
  public static final String COMMENT_LIKED = "blog.comment.liked";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void sendPostCreated(PostCreatedEvent event) {
    send(POST_CREATED, String.valueOf(event.getPostId()), event);
  }

  public void sendCommentCreated(CommentCreatedEvent event) {
    send(COMMENT_CREATED, String.valueOf(event.getPostId()), event);
  }

  public void sendPostLiked(LikeEvent event) {
    send(POST_LIKED, String.valueOf(event.getTargetId()), event);
  }

  public void sendCommentLiked(LikeEvent event) {
    send(COMMENT_LIKED, String.valueOf(event.getTargetId()), event);
  }

  // 파티션 키를 지정해 같은 게시글/댓글 이벤트는 같은 파티션으로 전송 (순서 보장)
  private void send(String topic, String key, Object event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(topic, key, payload)
          .addCallback(
              result -> log.info("[Kafka] published → topic={} key={}", topic, key),
              ex -> log.error("[Kafka] publish failed → topic={} cause={}", topic, ex.getMessage())
          );
    } catch (JsonProcessingException e) {
      log.error("[Kafka] serialization failed → event={} cause={}", event.getClass().getSimpleName(), e.getMessage());
    }
  }
}
