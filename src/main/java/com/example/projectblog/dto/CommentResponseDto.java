package com.example.projectblog.dto;

import com.example.projectblog.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;

    private Long userId;

    private Long postId;

    private String username;

    private String comment;

    private int commentLike;

    @Builder
    public CommentResponseDto(Long id, Long userId, Long postId, Comment comment, int commentLike) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.username = comment.getUsername();
        this.comment = comment.getComment();
        this.commentLike = commentLike;
    }
}
