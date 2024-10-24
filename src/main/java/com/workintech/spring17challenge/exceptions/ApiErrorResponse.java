package com.workintech.spring17challenge.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Data
@NoArgsConstructor
public class ApiErrorResponse {
    private  int status;
    private String message;
    private long timestamp;


    public ApiErrorResponse(String message, long timestamp, HttpStatus status) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status.value();
    }

}