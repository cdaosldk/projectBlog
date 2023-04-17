package com.example.projectblog.controller;

import com.example.projectblog.dto.LoginRequestDto;
import com.example.projectblog.dto.MessageResponseDto;
import com.example.projectblog.dto.SignupRequestDto;
import com.example.projectblog.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<MessageResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
    userService.signup(signupRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
      HttpServletResponse response) {
    userService.login(loginRequestDto, response);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponseDto> logout(HttpServletRequest request,
      HttpServletResponse response) {
    userService.logout(request, response);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}