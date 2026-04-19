package com.example.projectblog.util.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic postCreatedTopic() {
    return TopicBuilder.name("blog.post.created").partitions(3).replicas(1).build();
  }

  @Bean
  public NewTopic commentCreatedTopic() {
    return TopicBuilder.name("blog.comment.created").partitions(3).replicas(1).build();
  }

  @Bean
  public NewTopic postLikedTopic() {
    return TopicBuilder.name("blog.post.liked").partitions(3).replicas(1).build();
  }

  @Bean
  public NewTopic commentLikedTopic() {
    return TopicBuilder.name("blog.comment.liked").partitions(3).replicas(1).build();
  }
}
