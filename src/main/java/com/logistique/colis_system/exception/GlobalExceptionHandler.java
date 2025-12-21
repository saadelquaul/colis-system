package com.logistique.colis_system.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        ErrorResponse error =  ErrorResponse.builder().message(ex.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .error("Not Found").build();
        return new ResponseEntity<>(error , HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Business Conflict")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now()).build();

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Input validation failed")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleSecurityException(Exception ex, HttpServletRequest request) {
        HttpStatus status = (ex instanceof AccessDeniedException) ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;

        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error("Security Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(UsernameNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error =  ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                .error("Account Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        ErrorResponse error =  ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("Account Disabled")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        String message = "Erreur de format JSON: " + ex.getMostSpecificCause().getMessage();


        if (message.contains("Specialite")) {
            message = "La spécialité fournie est invalide. Valeurs acceptées: [STANDARD, FRAGILE, FRIGO]";
        }

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}
