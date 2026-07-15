package project.blog.domain.user.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import project.blog.domain.user.dto.LoginRequestDto;
import project.blog.domain.user.dto.SignupRequestDto;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    public ResponseEntity<byte[]> generateUserListOnExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] excelFile = excelService.writeExcelSheetToWorkbook(workbook);

        HttpHeaders headers = new HttpHeaders();
        String fileName = URLEncoder.encode("sample_data.xlsx", StandardCharsets.UTF_8.toString());
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(excelFile.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }

}