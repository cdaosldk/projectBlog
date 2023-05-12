package com.example.projectblog.domain.comment.repository;

import com.example.projectblog.domain.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  void deleteByCommentIdAndUserId(Long commentId, Long userId);

  boolean existsByCommentIdAndUserId(Long commentId, Long userId);

  int countAllByCommentId(Long commentid);
}
