package project.blog.domain.post.entity;

import project.common.Timestamped;
import project.blog.domain.comment.entity.Comment;
import project.blog.domain.post.dto.PostRequestDto;
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
@Entity(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String username;

  private String contents;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany
  private List<Comment> commentList = new ArrayList<>();

  public Post(PostRequestDto postRequestDto, User user) {
    this.title = postRequestDto.getTitle();
    this.username = user.getUsername();
    this.contents = postRequestDto.getContents();
    this.user = user;
  }

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.contents = postRequestDto.getContents();
  }
}
