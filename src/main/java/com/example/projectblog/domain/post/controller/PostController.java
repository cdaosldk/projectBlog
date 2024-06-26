package com.example.projectblog.domain.post.controller;

import com.example.projectblog.domain.post.dto.PostRequestDto;
import com.example.projectblog.domain.post.dto.PostResponseDto;
import com.example.projectblog.domain.post.service.PostService;
import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.util.exception.RestApiException;
import com.example.projectblog.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public ResponseEntity<MessageResponseDto> createPost(@RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(postService.createPost(postRequestDto, userDetails.getUser()));
  }

  @GetMapping("/posts")
  public Page<PostResponseDto> getPosts() {
    return postService.getPosts();
  }

  @GetMapping("/posts/{id}")
  public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
  }

  @PutMapping("/posts/{id}")
  public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
      @RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.update(id, postRequestDto, userDetails.getUser()));
  }

  @DeleteMapping("/posts/{id}")
  public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.delete(id, userDetails.getUser()));
  }

  @PostMapping("/posts/{id}")
  public ResponseEntity<MessageResponseDto> postLike(@PathVariable Long id,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok().body(postService.postLike(id, userDetails.getUser()));
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity handleException(IllegalArgumentException e) {
    RestApiException restApiException = RestApiException.builder()
        .errorMessage(e.getMessage())
        .httpStatus(HttpStatus.BAD_REQUEST)
        .build();
    return ResponseEntity.ok().body(restApiException);
  } // AOP : 이 클래스의 모든 메서드에 예외처리
}
