package com.avanade.rpg.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.avanade.rpg.exceptions.*;
import com.avanade.rpg.payloads.responses.StandardErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

class CustomExceptionHandlerTest {

    private final CustomExceptionHandler handler = new CustomExceptionHandler();

    @Test
    @DisplayName("Should handle BadRequestException and return BAD_REQUEST status")
    void shouldHandleBadRequestException() {
        BadRequestException exception = mock(BadRequestException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleBadRequestException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException and return NOT_FOUND status")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = mock(ResourceNotFoundException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleResourceNotFound(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle ResourceAlreadyExistsException and return BAD_REQUEST status")
    void shouldHandleResourceAlreadyExistsException() {
        ResourceAlreadyExistsException exception = mock(ResourceAlreadyExistsException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleResourceAlreadyExistsException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle UnknownViolationException and return INTERNAL_SERVER_ERROR status")
    void shouldHandleUnknownViolationException() {
        UnknownViolationException exception = mock(UnknownViolationException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleUnknownViolationException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException and return BAD_REQUEST status")
    void shouldHandleConstraintViolationException() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleConstraintViolationException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle NullPointerException and return INTERNAL_SERVER_ERROR status")
    void shouldHandleNullPointerException() {
        NullPointerException exception = mock(NullPointerException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleNullPointerException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException and return BAD_REQUEST status")
    void shouldHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleUnreadableMessage(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle HttpMessageNotWritableException and return BAD_REQUEST status")
    void shouldHandleHttpMessageNotWritableException() {
        HttpMessageNotWritableException exception = mock(HttpMessageNotWritableException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleUnreadableMessage(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException and return BAD_REQUEST status")
    void shouldHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        ResponseEntity<StandardErrorResponse> response = handler.handleMethodArgumentTypeMismatchException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return BAD_REQUEST status")
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");

        Mockito.when(exception.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<StandardErrorResponse> response = handler.handleInvalidMethodArguments(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
