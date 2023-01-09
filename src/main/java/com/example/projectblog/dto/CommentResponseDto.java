package com.example.projectblog.dto;

import com.example.projectblog.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String username;

    private String comment;

    private int likeComment;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(Comment comment, int likeComment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.comment = comment.getComment();
        this.likeComment = likeComment;
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
