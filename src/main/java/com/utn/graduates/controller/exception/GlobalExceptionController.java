package com.utn.graduates.controller.exception;

import com.utn.graduates.exception.CustomResponse;
import com.utn.graduates.exception.EventException;
import com.utn.graduates.exception.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    /**
     * Related to FileService
     * **/
    @ExceptionHandler(FileException.class)
    public ResponseEntity<CustomResponse> handleFileException(FileException ex) {
        return createCustomResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EventException.class)
    public ResponseEntity<CustomResponse> handleEventException(EventException ex) {
        return createCustomResponse(HttpStatus.BAD_REQUEST, ex.getMessage());

    }

    private ResponseEntity<CustomResponse> createCustomResponse(HttpStatus status, String message) {
        CustomResponse response = new CustomResponse(status, message);
        return new ResponseEntity<>(response, status);
    }
}
