package com.example.projectblog.controller;

import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.dto.PostRequestDto;
import com.example.projectblog.dto.PostResponseDto;
import com.example.projectblog.entity.Post;
import com.example.projectblog.exception.RestApiException;
import com.example.projectblog.security.UserDetailsImpl;
import com.example.projectblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.createPost(postRequestDto, userDetails.getUser()));
    }

    @GetMapping("/api/posts")
    public Page<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/api/posts/{id}")
    public Optional<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/api/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.update(id, postRequestDto, userDetails.getUser()));
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.delete(id, userDetails.getUser()));
    }

    @PostMapping("/api/posts/{id}")
    public ResponseEntity<MessageResponseDto> postLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postService.postLike(id, userDetails.getUser()));
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity handleException(IllegalArgumentException e) {
        RestApiException restApiException = RestApiException.builder()
                .errorMessage(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.ok().body(restApiException);
    } // AOP : 이 클래스의 모든 메서드에 예외처리
}
