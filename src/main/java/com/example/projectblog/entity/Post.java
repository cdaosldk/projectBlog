package com.example.projectblog.entity;

import com.example.projectblog.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId; // 유저 아이디값도 연관관계 설정하기 다대다 관계?? 나중에 연습하기

    @Column
    private String title;

    @Column
    private String username;

    @Column
    private String contents;

    @JsonManagedReference // 순환참조 문제
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")// 연관관계 설정, 비어있는 코멘트 리스트 생성, 영속성 관리
    private List<Comment> commentList = new ArrayList<>();

//    @Builder
    public Post(PostRequestDto postRequestDto, Long userId, List<Comment> commentList) {
        this.title = postRequestDto.getTitle();
        this.username = postRequestDto.getUsername();
        this.contents = postRequestDto.getContents();
        this.userId = userId;
        this.commentList = commentList;
    }

    public void add(Comment comment) {
        comment.setPost(this); // comment -> post 주입
        getCommentList().add(comment); // post의 commentList에 comment 주입
    }

//    public static Post createPost(String title, String username, String contents, Long userId) {
//        return Post.builder()
//                .userId(userId)
//                .title(title)
//                .username(username)
//                .contents(contents)
//                .build();
//    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.username = postRequestDto.getUsername();
        this.contents = postRequestDto.getContents();
    }
}
