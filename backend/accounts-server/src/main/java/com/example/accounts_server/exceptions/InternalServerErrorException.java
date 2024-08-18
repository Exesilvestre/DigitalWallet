package com.example.accounts_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}

