package com.utn.graduates.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomResponse {
    HttpStatus code;
    String message;

    public CustomResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatusCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
