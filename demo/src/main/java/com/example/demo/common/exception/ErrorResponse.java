package com.example.demo.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private LocalDateTime errorDT;
    private String reason;

    private ErrorResponse(LocalDateTime errorDT, String reason) {
        this.errorDT = errorDT;
        this.reason = reason;
    }

    public static ErrorResponse of(LocalDateTime errorDT, String reason) {
        return new ErrorResponse(errorDT, reason);
    }
}
