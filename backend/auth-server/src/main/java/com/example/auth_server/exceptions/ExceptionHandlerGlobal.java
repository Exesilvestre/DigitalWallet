package com.example.auth_server.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class ExceptionHandlerGlobal {
    private static final String EXCEPTION_HANDLED_BY = "(Rest)ResponseEntityExceptionHandler (@ControllerAdvice)";

    // Manejo de MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getField() + ": " + error.getDefaultMessage()));
        e.getBindingResult().getGlobalErrors().forEach(error ->
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "MethodArgumentNotValidException (overriden)",
                status,
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                errors
        );

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // Manejo de MissingServletRequestParameterException
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status,
                                                                       WebRequest request) {
        String error = e.getParameterName() + " parameter is missing";

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "MissingServletRequestParameterException (overriden)",
                status,
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    // Manejo de HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        String error = "Request body inexistente o mal formado";

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "HttpMessageNotReadableException (overriden)",
                status,
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    // Manejo de NoHandlerFoundException
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException e,
                                                       HttpHeaders headers,
                                                       HttpStatus status,
                                                       WebRequest request) {
        String error = "No handler found for " + e.getHttpMethod() + " " + e.getRequestURL();

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "NoHandlerFoundException (overriden)",
                status,
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    // Manejo de ResourceNotFoundException (custom)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e,
                                                         WebRequest request) {
        String error = e.getMessage();

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "(Custom)ResourceNotFoundException (@ExceptionHandler)",
                null,
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Manejo de BadRequestException (custom)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException e,
                                                   WebRequest request) {
        String error = e.getMessage();

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "(Custom)BadRequestException (@ExceptionHandler)",
                null,
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Manejo de ConstraintViolationException (validación de bean)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e,
                                                            HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> errors = new ArrayList<>();
        violations.forEach(violation ->
                errors.add(violation.getRootBeanClass().getName() + " " +
                        violation.getPropertyPath() + ": " + violation.getMessage()));

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "ConstraintViolationException (@ExceptionHandler)",
                null,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI(),
                e.getLocalizedMessage(),
                errors
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Manejo de MethodArgumentTypeMismatchException (conversión de tipo)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                   HttpServletRequest request) {
        String error = e.getName() + " should be of type " + e.getRequiredType().getName();

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "MethodArgumentTypeMismatchException (@ExceptionHandler)",
                null,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Manejo de cualquier otra excepción no manejada específicamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception e, HttpServletRequest request) {
        String error = "Error occurred";

        APIErrorEntity apiError = new APIErrorEntity(
                EXCEPTION_HANDLED_BY,
                "Exception (@ExceptionHandler)",
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI(),
                e.getLocalizedMessage(),
                error
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

