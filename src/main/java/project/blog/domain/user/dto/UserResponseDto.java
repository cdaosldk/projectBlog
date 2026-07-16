package project.blog.domain.user.dto;

import project.document.domain.excel.annotation.ExcelColumn;

public record UserResponseDto
    (
    @ExcelColumn(headerName = "사용자 ID", order = 1)
    Long id,
    @ExcelColumn(headerName = "사용자 이름", order = 2)
    String userName,
    @ExcelColumn(headerName = "사용자 이메일", order = 3)
    String email)
{}
