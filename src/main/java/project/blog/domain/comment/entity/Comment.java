package project.blog.domain.comment.entity;

import project.blog.Timestamped;
import project.blog.domain.comment.dto.CommentRequestDto;
import project.blog.domain.post.entity.Post;
import project.blog.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // 영속성 관리 문제, 지연 로딩
  @JoinColumn(name = "post_id", nullable = false) // 외래 키를 매핑할 때 사용, 외래 참조 키
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_id", nullable = false)
  private User user;

  @OneToMany
  private List<CommentComment> commentComment = new ArrayList<>();

  private String username;

  private String comment;

  public Comment(CommentRequestDto commentRequestDto, Post post, User user) {
    this.username = user.getUsername();
    this.comment = commentRequestDto.getComment();
    this.post = post;
    this.user = user;
  }

  public void update(CommentRequestDto commentRequestDto) {
    this.comment = commentRequestDto.getComment();
  }
}
