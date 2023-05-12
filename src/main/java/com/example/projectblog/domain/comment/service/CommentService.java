package com.example.projectblog.domain.comment.service;

import com.example.projectblog.domain.comment.dto.CommentRequestDto;
import com.example.projectblog.domain.comment.dto.CommentResponseDto;
import com.example.projectblog.domain.comment.entity.Comment;
import com.example.projectblog.domain.comment.entity.CommentLike;
import com.example.projectblog.domain.comment.repository.CommentLikeRepository;
import com.example.projectblog.domain.comment.repository.CommentRepository;
import com.example.projectblog.domain.post.entity.Post;
import com.example.projectblog.domain.post.repository.PostRepository;
import com.example.projectblog.domain.user.entity.User;
import com.example.projectblog.domain.user.entity.UserRoleEnum;
import com.example.projectblog.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  private final CommentLikeRepository commentLikeRepository;

  private final PostRepository postRepository;

  @Transactional
  public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto,
      User user) {
    Post post = postRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
    );

    Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

    int likeCount = 0;

    return new CommentResponseDto(comment, likeCount);
  }

  @Transactional
  public CommentResponseDto update(Long commentId, CommentRequestDto commentRequestDto, User user) {

    Comment comment;

    if (user.getRole().equals(UserRoleEnum.ADMIN)) {
      // 관리자일 경우
      comment = commentRepository.findById(commentId).orElseThrow(
          () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
      );
    } else { // 본인의 글이 아닐 경우
      comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
          () -> new IllegalArgumentException("본인의 댓글이 아닙니다.")
      );
    }

    comment.update(commentRequestDto);

    return new CommentResponseDto(comment,
        commentLikeRepository.countAllByCommentId(comment.getId()));
  }

  @Transactional
  public MessageResponseDto delete(Long commentId, User user) {

    // 객체 굳이 안만들어도 됨 ~ 사용하는 경우에만
    if (user.getRole().equals(UserRoleEnum.ADMIN)) {
      // 관리자일 경우
      commentRepository.findById(commentId).orElseThrow(
          () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
      );
    } else { // 본인의 글이 아닐 경우
      commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
          () -> new IllegalArgumentException("본인의 댓글이 아닙니다.")
      );
    }
    commentRepository.deleteById(commentId);
    return new MessageResponseDto("삭제 완료", HttpStatus.OK.value());
  }

  @Transactional(readOnly = true)
  public boolean checkCommentLike(Long commentId, User user) {
    // 해당 회원의 좋아요 여부 확인
    return commentLikeRepository.existsByCommentIdAndUserId(commentId, user.getId());
  }

  @Transactional
  public MessageResponseDto commentLike(Long commentId, User user) {
    // 입력받은 댓글 아이디와 일치하는 아이디가 있는 지 DB 조회
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
    );

    // 해당 회원의 좋아요 여부를 확인하고 비어있으면 좋아요, 아니면 좋아요 취소
    if (!checkCommentLike(commentId, user)) {
      commentLikeRepository.save(new CommentLike(comment, user));
      return new MessageResponseDto("좋아요 완료", HttpStatus.OK.value());
    } else {
      commentLikeRepository.deleteByCommentIdAndUserId(commentId, user.getId());
      return new MessageResponseDto("좋아요 취소", HttpStatus.OK.value());
    }


  }
}
