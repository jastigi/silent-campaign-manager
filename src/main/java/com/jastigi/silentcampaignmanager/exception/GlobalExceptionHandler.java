package com.jastigi.silentcampaignmanager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.data.mapping.PropertyReferenceException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(CampaignNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCampaignNotFound(
                        CampaignNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setError("Not Found");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationErrors(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(error.getField(),
                                                error.getDefaultMessage()));

                return ResponseEntity.badRequest()
                                .body(errors);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
                        MethodArgumentTypeMismatchException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setError("Bad Request");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(PropertyReferenceException.class)
        public ResponseEntity<ErrorResponse> handlePropertyReference(
                        PropertyReferenceException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setError("Bad Request");
                error.setMessage("Invalid sort property: " + ex.getPropertyName());
                error.setPath(request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleInvalidCredentials(
                        InvalidCredentialsException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.UNAUTHORIZED.value());
                error.setError("Unauthorized");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(PatrolNotFoundException.class)
        public ResponseEntity<ErrorResponse> handlePatrolNotFound(
                        PatrolNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setError("Not Found");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(SubmarineNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleSubmarineNotFound(
                        SubmarineNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setError("Not Found");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(ContactNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleContactNotFound(
                        ContactNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setError("Not Found");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(PatrolEventNotFoundException.class)
        public ResponseEntity<ErrorResponse> handlePatrolEventNotFound(
                        PatrolEventNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = new ErrorResponse();

                error.setTimestamp(LocalDateTime.now());
                error.setStatus(HttpStatus.NOT_FOUND.value());
                error.setError("Not Found");
                error.setMessage(ex.getMessage());
                error.setPath(request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

}
