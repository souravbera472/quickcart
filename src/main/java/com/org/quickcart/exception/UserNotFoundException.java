package com.org.quickcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String resourceName, String fieldName, String fieldValue){
        super(resourceName + " not found with given " + fieldName + " is " + fieldValue);
    }
}
