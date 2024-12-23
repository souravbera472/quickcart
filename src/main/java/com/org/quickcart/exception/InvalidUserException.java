package com.org.quickcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserException extends RuntimeException{
    public InvalidUserException(){
        super("Unable to authenticate user. Invalid user!");
    }
}
