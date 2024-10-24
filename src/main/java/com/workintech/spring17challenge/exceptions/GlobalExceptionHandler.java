package com.workintech.spring17challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception) {
        ApiErrorResponse apiResponse = new ApiErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis(),
                exception.getHttpStatus()
        );
        return new ResponseEntity<>(apiResponse, exception.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception) {
        ApiErrorResponse apiResponse = new ApiErrorResponse(
                "An unexpected error occurred: " + exception.getMessage(),
                System.currentTimeMillis(),
                HttpStatus.INTERNAL_SERVER_ERROR // Adjust as needed
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
