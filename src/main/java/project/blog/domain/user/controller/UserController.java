package project.blog.domain.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import project.blog.domain.user.dto.LoginRequestDto;
import project.blog.domain.user.dto.SignupRequestDto;
import project.blog.domain.user.dto.UserResponseDto;
import project.blog.domain.user.service.UserService;
import project.common.dto.MessageResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.document.domain.excel.service.ExcelService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final ExcelService excelService;
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

    @GetMapping("/generate/list/excel")
    public ResponseEntity<byte[]> generateUserListOnExcel() throws IOException, IllegalAccessException {
        List<UserResponseDto> dataList = userService.getUserList();

        int minWidth = 12; // 컬럼 최소 너비 (글자수 기준)

        byte[] excelFile = excelService.generateExcelFile("사용자_목록", minWidth, dataList);

        HttpHeaders headers = new HttpHeaders();
        String fileName = URLEncoder.encode("user_list.xlsx", StandardCharsets.UTF_8);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(excelFile.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }
}