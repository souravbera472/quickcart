package com.org.quickcart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT("QCERR001", "Invalid input provided", "Please provide valid input",
            HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("QCERR002", "User not found", "User name is invalid", HttpStatus.NOT_FOUND),
    SERVER_ERROR("QCERR003", "Internal server error", "The Service in Unavailable",
            HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("QCERR004", "Unauthorized request", "The request is Unauthorized",
            HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String developerMessage;
    private final String userMessage;
    private final HttpStatus httpStatus;

}
