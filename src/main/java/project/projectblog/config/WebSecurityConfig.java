package project.projectblog.config;

import project.projectblog.util.jwt.JwtAuthFilter;
import project.projectblog.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // 6.x 버전 변경: @EnableGlobalMethodSecurity deprecated 대체
public class WebSecurityConfig {

  private final JwtUtil jwtUtil;

  @Bean // Spring Security가 제공하는 적응형 단방향 함수인 bCrypt를 사용해 비밀번호를 암호화
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean // WebSecurityCustomizer는 SecurityFilterChain보다 우선 적용되어 FilterChain을 통한 검사 시 적용된 상태로 검사를 수행
  public WebSecurityCustomizer webSecurityCustomizer() {
    // h2-console 사용 및 resources 접근 허용 설정
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // 1. CSRF 비활성화 (JWT 사용하므로 비활성화)
        .csrf(AbstractHttpConfigurer::disable)

        // 2. 세션 정책 설정 (STATELESS)
        .sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // 3. 인가(Authorization) 설정 (antMatchers -> requestMatchers로 변경)
        .authorizeHttpRequests(authorizeHttpRequests ->
            authorizeHttpRequests
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers("/image-upload").permitAll()
                .anyRequest().authenticated()
        )

        // 4. FormLogin 설정
        .formLogin(formLogin ->
            formLogin.permitAll()
        )

        // 5. 예외 처리 설정
        .exceptionHandling(exceptionHandling ->
            exceptionHandling.accessDeniedPage("/api/user/forbidden")
        )

        // 6. JWT 커스텀 필터 등록
        .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}