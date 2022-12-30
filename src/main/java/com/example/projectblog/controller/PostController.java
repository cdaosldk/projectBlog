package com.example.projectblog.controller;

import com.example.projectblog.dto.PostRequestDto;
import com.example.projectblog.dto.PostResponseDto;
import com.example.projectblog.entity.Post;
import com.example.projectblog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.createPost(postRequestDto, request);
    }

    @GetMapping("/api/posts")
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/api/posts/{id}")
    public Optional<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/api/posts/{id}")
    public void updatePost(@PathVariable Long id, HttpServletRequest request, @RequestBody PostRequestDto postRequestDto) {
        postService.update(id, request, postRequestDto);
    }
//
//    @DeleteMapping("/api/posts/{id}")
//    public String deletePost(@PathVariable Long id, HttpServletRequest request) {
//        return postService.delete(id, request);
//    }
}
