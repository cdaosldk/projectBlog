package com.example.projectblog.controller;

import com.example.projectblog.dto.CommentRequestDto;
import com.example.projectblog.dto.CommentResponseDto;
import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.exception.RestApiException;
import com.example.projectblog.security.UserDetailsImpl;
import com.example.projectblog.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{post_id}/comments")
    public ResponseEntity createComment(@PathVariable Long post_id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return ResponseEntity.ok().body(commentService.createComment(post_id, commentRequestDto, userDetails.getUser()));
        } catch (IllegalArgumentException e) {
            RestApiException restApiException = RestApiException.builder()
                    .errorMessage(e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
            return ResponseEntity.ok().body(restApiException);
        }
    }

    @PutMapping("/api/posts/{id}/comments/commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.update(commentId, commentRequestDto, userDetails.getUser()));
    }

    @DeleteMapping("/api/posts/{id}/comments/{commentId}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long id, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.delete(commentId, userDetails.getUser()));
    }

    @PatchMapping("/api/comments/{commentId}")
    public ResponseEntity<MessageResponseDto> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(commentService.commentLike(commentId, userDetails.getUser()));
    }
}
