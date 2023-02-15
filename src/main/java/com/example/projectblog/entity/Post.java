package com.example.projectblog.entity;

import com.example.projectblog.dto.PostRequestDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
