package com.example.projectblog.domain.comment.repository;

import com.example.projectblog.domain.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}
