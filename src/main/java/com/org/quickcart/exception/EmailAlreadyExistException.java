package com.org.quickcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException(String email){
        super("Email already exist with given email is " + email);
    }
}
