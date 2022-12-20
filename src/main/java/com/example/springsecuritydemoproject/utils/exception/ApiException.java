package com.example.springsecuritydemoproject.utils.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.lang.String.format;
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static ApiException build(HttpStatus status, String message, Object... args) {
        throw new ApiException(status, format(message, args));
    }

}
