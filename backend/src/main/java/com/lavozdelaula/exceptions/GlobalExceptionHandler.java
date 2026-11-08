package com.lavozdelaula.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return response(HttpStatus.BAD_REQUEST, "Validation Error", details, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Bad Request",
                "El parámetro '" + ex.getName() + "' tiene un formato inválido.", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Bad Request",
                "El cuerpo de la solicitud no contiene JSON válido.", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Conflict",
                "La operación viola una restricción de integridad o un dato único ya existe.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {
        LOGGER.error("Error inesperado procesando {} {}", request.getMethod(), request.getRequestURI(), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ocurrió un error inesperado.", request);
    }

    private ResponseEntity<ApiError> response(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
