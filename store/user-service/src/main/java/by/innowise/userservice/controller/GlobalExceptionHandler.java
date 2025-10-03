package by.innowise.userservice.controller;

import by.innowise.userservice.exception.EntityNotFoundException;
import by.innowise.userservice.model.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex,
      HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .title("Entity Not Found")
        .name(ex.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
      HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .title("Entity Not Found")
        .name(ex.getClass().getSimpleName())
        .status(HttpStatus.NOT_FOUND.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    String errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining("; "));

    ErrorResponse response = ErrorResponse.builder()
        .title("Validation Failed")
        .name(ex.getClass().getSimpleName())
        .status(HttpStatus.BAD_REQUEST.value())
        .message(errors)
        .path(request.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .title("Internal Server Error")
        .name(ex.getClass().getSimpleName())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
