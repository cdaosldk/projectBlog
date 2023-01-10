package com.example.projectblog.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class RestApiException {

    private String errorMessage;
    private HttpStatus httpStatus;

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Builder
    public RestApiException(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
