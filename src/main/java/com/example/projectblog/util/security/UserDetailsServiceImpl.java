package com.example.projectblog.util.security;

import com.example.projectblog.domain.user.entity.User;
import com.example.projectblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
// 초기화 되지 않은 final 필드나, @NonNUll 값이 붙은 필드에 대한 생성자를 만들어준다.
@RequiredArgsConstructor
// 필드 값에 final이 붙지 않는 경우와 붙는 경우 왜 어노테이션의 차이가 있을까?
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
    );

    return new UserDetailsImpl(user, user.getUsername());
  }
}
