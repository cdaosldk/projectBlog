package project.projectblog.domain.user.entity;

import project.projectblog.domain.comment.entity.Comment;
import project.projectblog.domain.post.entity.Post;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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