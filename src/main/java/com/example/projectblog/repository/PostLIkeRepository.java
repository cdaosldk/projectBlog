package com.example.projectblog.repository;

import com.example.projectblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLIkeRepository extends JpaRepository<Post, Long> {

    void deleteByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    int countAllByPostId(Long postid);
}
