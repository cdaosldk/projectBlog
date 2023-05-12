package com.example.projectblog.domain.user.service;

import com.example.projectblog.domain.user.dto.LoginRequestDto;
import com.example.projectblog.domain.user.dto.SignupRequestDto;
import com.example.projectblog.domain.user.entity.User;
import com.example.projectblog.domain.user.entity.UserRoleEnum;
import com.example.projectblog.domain.user.repository.UserRepository;
import com.example.projectblog.util.jwt.JwtAuthFilter;
import com.example.projectblog.util.jwt.JwtUtil;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final JwtUtil jwtUtil;

  private final JwtAuthFilter jwtAuthFilter;

  private final RefreshTokenService refreshTokenService;

  private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

  @Transactional
  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String email = signupRequestDto.getEmail();

    findByUsername(username);

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
    userRepository.save(new User(username, password, email, role));
  }

  @Transactional
  public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    User user = findByUsername(loginRequestDto.getUsername());

    // 비밀번호 확인
    if (!user.getPassword().equals(loginRequestDto.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    // token 발급
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createAccessToken(user.getUsername()));

    response.addCookie(
        refreshTokenService.createRefreshCookie(
            refreshTokenService.createRefreshToken(user.getUsername())));
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    refreshTokenService.deleteRefreshToken(refreshTokenService.findById(
        jwtAuthFilter.separateRefreshToken(
            jwtAuthFilter.resolveRefreshTokenFromCookies(request))[0]));
  }

  private User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다"));
  }
}