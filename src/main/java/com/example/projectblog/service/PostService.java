package com.example.projectblog.service;

import com.example.projectblog.dto.PostRequestDto;
import com.example.projectblog.dto.PostResponseDto;
import com.example.projectblog.entity.Comment;
import com.example.projectblog.entity.Post;
import com.example.projectblog.entity.User;
import com.example.projectblog.entity.UserRoleEnum;
import com.example.projectblog.jwt.JwtUtil;
import com.example.projectblog.repository.PostLIkeRepository;
import com.example.projectblog.repository.PostRepository;
import com.example.projectblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLIkeRepository postLIkeRepository;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

        int likePost = 0;

        Post post = postRepository.save(new Post(postRequestDto, user));

        return new PostResponseDto(post, likePost);
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc();
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

        return new PostResponseDto(post, postLIkeRepository.countAllByPostId(id));

    }

    @Transactional
    public String delete(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
                );

                Post post = postRepository.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 글입니다.")
                );

                // 사용자 권한 가져오기
                UserRoleEnum userRoleEnum = user.getRole();
                System.out.println("role = " + userRoleEnum);

                if (userRoleEnum == UserRoleEnum.ADMIN) {
                    // 관리자일 경우
                    postRepository.deleteById(id);

                } else if (userRoleEnum == UserRoleEnum.USER && user.getUsername().equals(post.getUsername())) { // 사용자 권한이 USER일 경우
                    postRepository.deleteById(id);
                } else { // 본인의 글이 아닐 경우
                    throw new IllegalArgumentException("본인의 글이 아닙니다.");
                }
            }
            return "삭제되었습니다.";
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다. StatusCode 400");
        }
    }
}
