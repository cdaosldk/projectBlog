package com.example.projectblog.controller;

import com.example.projectblog.dto.CommentRequestDto;
import com.example.projectblog.dto.CommentResponseDto;
import com.example.projectblog.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{post_id}/comments")
    public CommentResponseDto createComment(@PathVariable Long post_id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String username = commentRequestDto.getUsername();
        String comment = commentRequestDto.getComment();
        return commentService.createComment(post_id, username, comment, request);
    }

    @PutMapping("/api/posts/{id}/comments/{commentId}")
    public void updateComment(@PathVariable Long id, @PathVariable Long commentId, HttpServletRequest request, @RequestBody CommentRequestDto commentRequestDto) {
        commentService.update(id, commentId, request, commentRequestDto);
    }

    @DeleteMapping("/api/posts/{id}/comments/{commentId}")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId,HttpServletRequest request) {
        return commentService.delete(id, commentId, request);
    }
}
