package project.blog.domain.comment.repository;

import project.blog.domain.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  void deleteByCommentIdAndUserId(Long commentId, Long userId);

  boolean existsByCommentIdAndUserId(Long commentId, Long userId);

  int countAllByCommentId(Long commentid);
}
