package com.example.projectblog.entity;

import com.example.projectblog.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference // 순환참조 문제
    @ManyToOne(fetch = FetchType.LAZY) // 영속성 관리 문제
    @JoinColumn(name = "post_id", nullable = false) // 외래 키를 매핑할 때 사용, 외래 참조 키
    @Setter
    private Post post;

    @Column
    private String username;

    @Column
    private String comment;

    @Builder
    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.username = commentRequestDto.getUsername();
        this.comment = commentRequestDto.getComment();
    }
}
