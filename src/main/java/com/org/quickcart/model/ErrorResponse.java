package com.org.quickcart.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String developerMessage;
    private String userMessage;
    private Map<String, String> validationErrors;

    public ErrorResponse(String errorCode, String developerMessage, String userMessage) {
        this.errorCode = errorCode;
        this.developerMessage = developerMessage;
        this.userMessage = userMessage;
    }

}
