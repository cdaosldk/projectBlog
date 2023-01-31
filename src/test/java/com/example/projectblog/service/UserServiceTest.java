package com.example.projectblog.service;

import com.example.projectblog.dto.SignupRequestDto;
import com.example.projectblog.entity.User;
import com.example.projectblog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

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