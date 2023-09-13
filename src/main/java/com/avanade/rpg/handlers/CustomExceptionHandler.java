package com.avanade.rpg.handlers;

import com.avanade.rpg.exceptions.*;
import com.avanade.rpg.payloads.responses.StandardErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardErrorResponse> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandardErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownViolationException.class)
    public ResponseEntity<StandardErrorResponse> handleUnknownViolationException(UnknownViolationException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorResponse> handleInvalidMethodArguments(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, String.join("; ", errorMessages));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardErrorResponse> handleNullPointerException(NullPointerException ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<StandardErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        return buildErrorResponse(ex, status, ex.getMessage());
    }

    private ResponseEntity<StandardErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, String message) {
        LOGGER.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        StandardErrorResponse error = new StandardErrorResponse(
                status.value(),
                message,
                new Date().toString()
        );
        return ResponseEntity.status(status).body(error);
    }
}