package com.example.projectblog.domain.post.repository;

import com.example.projectblog.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  void deleteByPostIdAndUserId(Long postId, Long userId);

  boolean existsByPostIdAndUserId(Long postId, Long userId);

  int countAllByPostId(Long postid);
}
