package com.example.bck.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    log.error("Internal server error: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    log.warn("Bad request: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
    log.error("An error occurred: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(new ErrorResponse("An error occurred", request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private static class ErrorResponse {
    private final String message;
    private final String details;

    public ErrorResponse(String message, String details) {
      this.message = message;
      this.details = details;
    }

    public String getMessage() {
      return message;
    }

    public String getDetails() {
      return details;
    }
  }
}