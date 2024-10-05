package com.example.thehiveapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<CustomErrorResponse> handleMissingRequestBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
//        CustomErrorResponse response = new CustomErrorResponse();
//
//        response.setTimestamp(LocalDateTime.now());
//        response.setStatus(HttpStatus.BAD_REQUEST.value());
//        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
//        response.setMessage("Required request body is missing.");
//        response.setPath(request.getRequestURI());
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return Map.of("messages", errors);
    }
}



