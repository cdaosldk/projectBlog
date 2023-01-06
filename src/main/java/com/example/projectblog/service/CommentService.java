package com.example.projectblog.service;

import com.example.projectblog.dto.CommentRequestDto;
import com.example.projectblog.dto.CommentResponseDto;
import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.entity.*;
import com.example.projectblog.jwt.JwtUtil;
import com.example.projectblog.repository.CommentLikeRepository;
import com.example.projectblog.repository.CommentRepository;
import com.example.projectblog.repository.PostRepository;
import com.example.projectblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(Long post_id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 올바른 토큰인지 확인
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다. StatusCode 400");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.findById(post_id).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 글입니다.")
            );


            Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

            int likeCount = 0;

            post.add(comment);

            return new CommentResponseDto(comment, likeCount);
        } else {
            return null;
        }
    }

    @Transactional
    public void update(Long id, Long commentId, HttpServletRequest request, CommentRequestDto commentRequestDto) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다. StatusCode 400");
            }

            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
            );

            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
            );

            // 사용자 권한 가져오기
            UserRoleEnum userRoleEnum = user.getRole();
            System.out.println("role = " + userRoleEnum);

            if (userRoleEnum == UserRoleEnum.ADMIN) {
                // 관리자일 경우
                comment.update(commentRequestDto);

            } else if (userRoleEnum == UserRoleEnum.USER && user.getUsername().equals(comment.getUsername())) { // 사용자 권한이 USER일 경우
                comment.update(commentRequestDto);
            } else { // 본인의 글이 아닐 경우
                throw new IllegalArgumentException("본인의 댓글이 아닙니다.");
            }
        }
    }

    @Transactional
    public String delete(Long id, Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
                );

                Comment comment = commentRepository.findById(commentId).orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
                );

                // 사용자 권한 가져오기
                UserRoleEnum userRoleEnum = user.getRole();
                System.out.println("role = " + userRoleEnum);

                if (userRoleEnum == UserRoleEnum.ADMIN) {
                    // 관리자일 경우
                    commentRepository.deleteById(commentId);

                } else if (userRoleEnum == UserRoleEnum.USER && user.getUsername().equals(comment.getUsername())) { // 사용자 권한이 USER일 경우
                    commentRepository.deleteById(commentId);
                } else { // 본인의 글이 아닐 경우
                    throw new IllegalArgumentException("본인의 댓글이 아닙니다.");
                }
            }
            return "삭제되었습니다.";
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다. StatusCode 400");
        }
    }

    @Transactional(readOnly = true)
    public boolean checkCommentLike(Long commentId, User user) {
        // 해당 회원의 좋아요 여부 확인
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, user.getId());
    }

    @Transactional
    public MessageResponseDto commentLike(Long commentId, User user) {
        // 입력받은 댓글 아이디와 일치하는 아이디가 있는 지 DB 조회
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        // 해당 회원의 좋아요 여부를 확인하고 비어있으면 좋아요, 아니면 좋아요 취소
        if(!checkCommentLike(commentId, user)) {
            commentLikeRepository.saveAndFlush(new CommentLike(comment, user));
            return new MessageResponseDto("좋아요 완료", HttpStatus.OK.value());
        } else {
            commentLikeRepository.deleteByCommentIdAndUserId(commentId, user.getId());
            return new MessageResponseDto("좋아요 취소", HttpStatus.OK.value());
        }


    }
}
