package com.example.projectblog.controller;

import com.example.projectblog.dto.LoginRequestDto;
import com.example.projectblog.dto.SignupRequestDto;
import com.example.projectblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "success";
    }

    @ResponseBody // ajax 형식으로 body를 받아오기 떄문에 body 어노테이션을 써줘야한다.
    @PostMapping("/login") // request에서 헤더가 넘어온 것을 받아오는 것처럼 토큰을 반환할때는 response 객체로 반환,
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return "success";
    }

}