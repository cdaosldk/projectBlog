package com.example.projectblog.domain.comment.cotroller;

import com.example.projectblog.domain.comment.dto.CommentRequestDto;
import com.example.projectblog.domain.comment.dto.CommentResponseDto;
import com.example.projectblog.domain.comment.service.CommentService;
import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.util.exception.RestApiException;
import com.example.projectblog.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/api/posts/{post_id}/comments")
  public ResponseEntity createComment(@PathVariable Long post_id,
      @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    try {
      return ResponseEntity.ok()
          .body(commentService.createComment(post_id, commentRequestDto, userDetails.getUser()));
    } catch (IllegalArgumentException e) {
      RestApiException restApiException = RestApiException.builder()
          .errorMessage(e.getMessage())
          .httpStatus(HttpStatus.BAD_REQUEST)
          .build();
      return ResponseEntity.ok().body(restApiException);
    } // 일일히 모든 메서드에 예외처리
  }

  @PutMapping("/api/{postId}/comments/{commentId}")
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
      @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok()
        .body(commentService.update(commentId, commentRequestDto, userDetails.getUser()));
  }

  @DeleteMapping("/api/{postId}/comments/{commentId}")
  public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long postId,
      @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(commentService.delete(commentId, userDetails.getUser()));
  }

  @PostMapping("/api/comments/{commentId}")
  public ResponseEntity<MessageResponseDto> commentLike(@PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(commentService.commentLike(commentId, userDetails.getUser()));
  }

}
