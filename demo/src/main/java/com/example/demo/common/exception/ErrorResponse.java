package com.example.demo.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private LocalDateTime errorDT;
    private ExceptionCode exceptionCode;

    private ErrorResponse(LocalDateTime errorDT, ExceptionCode exceptionCode) {
        this.errorDT = errorDT;
        this.exceptionCode = exceptionCode;
    }

    public static ErrorResponse of(LocalDateTime errorDT, ExceptionCode exceptionCode) {
        return new ErrorResponse(errorDT, exceptionCode);
    }
}
