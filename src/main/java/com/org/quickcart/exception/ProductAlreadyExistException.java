package com.org.quickcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductAlreadyExistException extends RuntimeException{

    public ProductAlreadyExistException(String name){
        super("Product already exist with given name is " + name);
    }
}
