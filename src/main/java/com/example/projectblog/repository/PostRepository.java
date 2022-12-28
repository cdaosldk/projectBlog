package com.example.projectblog.repository;

import com.example.projectblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
// 레포지토리가 인터페이스로 설계되어야 하는 이유? JPA 이전에 다른 DB로 결정할 수도 있기 때문에 변경이 쉬운 인터페이스로 만드는 것이다.
// 옵셔널 타입의 사용 메커니즘을 모르고 사용했음

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();

    Optional<Post> findById(Long id);

}