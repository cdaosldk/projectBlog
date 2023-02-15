package com.example.projectblog.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany
  private List<Post> postList = new ArrayList<>();

  @OneToMany
  private List<Comment> commentList = new ArrayList<>();

  // nullable: null 허용 여부
  // unique: 중복 허용 여부 (false 일때 중복 허용)
  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;

  public User(String username, String password, String email, UserRoleEnum role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }
}