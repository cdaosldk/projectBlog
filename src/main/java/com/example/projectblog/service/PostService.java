package com.example.projectblog.service;

import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.dto.PostRequestDto;
import com.example.projectblog.dto.PostResponseDto;
import com.example.projectblog.entity.*;
import com.example.projectblog.repository.PostLikeRepository;
import com.example.projectblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

        int likePost = 0;

        Post post = postRepository.save(new Post(postRequestDto, user));

        return new PostResponseDto(post, likePost);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPosts() {
        return postRepository.findAll(PageRequest.of(0,10))
                .map(post -> new PostResponseDto(post, postLikeRepository.countAllByPostId(post.getId())));
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, User user) {

        Post post;

        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            post = postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 게시물입니다")
            );
        } else {
            post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("본인의 게시물이 아닙니다")
            );
        }

        post.update(postRequestDto);

        return new PostResponseDto(post, postLikeRepository.countAllByPostId(id));

    }

    @Transactional
    public MessageResponseDto delete(Long id, User user) {

        // 객체 굳이 안만들어도 됨 ~ 사용하는 경우에만
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            // 관리자일 경우
            postRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
            );
        } else { // 본인의 글이 아닐 경우
            postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("본인의 게시물이 아닙니다.")
            );
        }
        postRepository.deleteById(id);
        return new MessageResponseDto("삭제 완료", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public boolean checkPostLike(Long id, User user) {
        return postLikeRepository.existsByPostIdAndUserId(id, user.getId());
    }

    @Transactional
    public MessageResponseDto postLike(Long id, User user) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );

        if(!checkPostLike(id, user)) {
            postLikeRepository.save(new PostLike(post, user));
            return new MessageResponseDto("좋아요 완료", HttpStatus.OK.value());
        } else {
            postLikeRepository.deleteByPostIdAndUserId(id, user.getId());
            return  new MessageResponseDto("좋아요 취소", HttpStatus.OK.value());
        }
    }
}
