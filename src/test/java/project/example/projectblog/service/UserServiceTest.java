package project.example.projectblog.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import project.blog.domain.user.dto.SignupRequestDto;
import project.blog.domain.user.entity.User;
import project.blog.domain.user.repository.UserRepository;
import project.blog.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void signup() {
    // given
    SignupRequestDto requestDto = SignupRequestDto.builder()
        .admin(false)
        .username("nathan")
        .password("1234qwer!@")
        .email("nathan@gmail.com")
        .adminToken("")
        .build();

    // when
    userService.signup(requestDto);

    // then
    verify(userRepository).save(any(User.class));
  }
}