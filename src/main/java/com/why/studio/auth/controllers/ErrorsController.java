package com.why.studio.auth.controllers;

import com.why.studio.auth.dto.ErrorObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorsController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.unprocessableEntity().body(
                ErrorObject.builder()
                        .errors(errors.entrySet().stream()
                                .map(Object::toString)
                                .collect(Collectors.toList()))
                        .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorObject> handleValidationException(Exception ex) {
        return ResponseEntity.unprocessableEntity().body(
                ErrorObject.builder()
                        .errors(List.of(ex.getMessage()))
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorObject> handleResponseStatusException(Exception ex) {
        String reason = ((ResponseStatusException) ex).getReason();
        return ResponseEntity.status(((ResponseStatusException) ex).getStatus())
                .body(
                        ErrorObject.builder()
                                .errors(
                                        List.of(
                                                Optional.ofNullable(reason)
                                                        .orElse(ex.getMessage())))
                                .build());
    }

}
