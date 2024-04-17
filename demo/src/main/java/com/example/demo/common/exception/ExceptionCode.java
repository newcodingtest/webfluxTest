package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    BOOK_NOT_FOUND(404, "Book not found"),
    BOOK_EXISTS(409, "Book exists");

    private int status;

    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
