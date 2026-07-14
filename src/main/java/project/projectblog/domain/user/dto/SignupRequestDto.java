package project.projectblog.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {

  private String username;
  private String password;
  private String email;
  private boolean admin;
  private String adminToken;

}