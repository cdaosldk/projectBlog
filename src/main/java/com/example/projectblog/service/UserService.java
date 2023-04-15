package com.example.projectblog.service;

import com.example.projectblog.dto.LoginRequestDto;
import com.example.projectblog.dto.SignupRequestDto;
import com.example.projectblog.entity.User;
import com.example.projectblog.entity.UserRoleEnum;
import com.example.projectblog.jwt.JwtUtil;
import com.example.projectblog.repository.UserRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

  @Transactional
  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String email = signupRequestDto.getEmail();

    // 회원 중복 확인
    Optional<User> found = userRepository.findByUsername(username);
    if (found.isPresent()) {
      throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
    }

    // 등록 시 제한사항 설정 및 확인
    String usernamePattern = "^[a-z0-9]{4,10}$";
    String pwdPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$";
    if (!Pattern.matches(usernamePattern, username)) {
      throw new IllegalArgumentException("조건에 일지하지 않는 아이디입니다. 아이디를 다시 확인해주세요.");
    }
    if (!Pattern.matches(pwdPattern, password)) {
      throw new IllegalArgumentException("조건에 일지하지 않는 비밀번호입니다. 비밀번호를 다시 확인해주세요.");
    }

    // 사용자 ROLE 확인
    UserRoleEnum role = UserRoleEnum.USER;
    if (signupRequestDto.isAdmin()) {
      if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
        throw new IllegalArgumentException("관리자 암호가 틀려 관리자로 등록이 불가능합니다.");
      }
      role = UserRoleEnum.ADMIN;
    }
    if (signupRequestDto.getAdminToken().equals("")) {
      role = UserRoleEnum.USER;
    }
    User user = new User(username, password, email, role);
    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    String username = loginRequestDto.getUsername();
    String password = loginRequestDto.getPassword();

    // 사용자 확인
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
    );

    // 비밀번호 확인
    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    // token 발급
    String accessToken = jwtUtil.createAccessToken(user.getUsername());
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
    String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
    response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);
  }
}