package com.example.sales_report.exception;

import com.example.sales_report.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handle(Exception ex) {
        return ResponseEntity.internalServerError().body(
                new ApiResponse<>("ERROR", ex.getMessage(), LocalDateTime.now())
        );
    }
}
