package com.org.quickcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String id){
        super("Product not found with given id is " + id);
    }
}
