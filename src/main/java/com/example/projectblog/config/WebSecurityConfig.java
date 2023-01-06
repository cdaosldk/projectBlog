package com.example.projectblog.config;

import com.example.projectblog.jwt.JwtAuthFilter;
import com.example.projectblog.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원 가능
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
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
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // permitAll()을 사용하여 해당 URL을 인증하지 않고 요청에 응답
        http.authorizeRequests().antMatchers("/api/user/**").permitAll()
                .anyRequest().authenticated()
                // Jwt 토큰을 활용한 인증/인가 설정, CustomFilter 틍록, addFilterBefore(추가할 필터, 추가되는 필터의 다음 필터)
                // UsernamePasswordAuthenticationFilter : FormLogin 형식을 사용할 때 username과 password를 사용한 인증
                // 인증에 실패한다면, 기본 로그인 페이지를 반환
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 스프링 시큐리티에서 제공하는 기본 FormLogin 방식을 사용하는 로그인페이지의 요청을 인증없이 허용
        http.formLogin().permitAll();

        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");

        return http.build();
    }

}