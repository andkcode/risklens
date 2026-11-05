package com.risklens.core.common;

import com.risklens.core.exception.UserAlreadyExistsException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Validation failed for one or more fields");
    problemDetail.setType(URI.create("https://api.risklens.com/errors/validation"));
    problemDetail.setTitle("Bad Request");
    problemDetail.setInstance(URI.create(getRequestUri(request)));

    List<ValidationError> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
            .toList();

    problemDetail.setProperty("errors", errors);
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatusException(
      ResponseStatusException ex, WebRequest request) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            ex.getStatusCode(), ex.getReason() != null ? ex.getReason() : ex.getMessage());
    problemDetail.setType(
        URI.create("https://api.risklens.com/errors/" + ex.getStatusCode().value()));
    problemDetail.setTitle(ex.getStatusCode().toString());
    problemDetail.setInstance(URI.create(getRequestUri(request)));
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return problemDetail;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    problemDetail.setType(URI.create("https://api.risklens.com/errors/409"));
    problemDetail.setTitle("Conflict");
    problemDetail.setInstance(URI.create(getRequestUri(request)));
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return problemDetail;
  }

  @ExceptionHandler(OpenApiResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(
      OpenApiResourceNotFoundException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setType(URI.create("https://api.risklens.com/errors/404"));
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setInstance(URI.create(getRequestUri(request)));
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return problemDetail;
  }

  @ExceptionHandler(BadRequestException.class)
  public ProblemDetail handleBadRequestException(BadRequestException ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setType(URI.create("https://api.risklens.com/errors/400"));
    problemDetail.setTitle("Bad Request");
    problemDetail.setInstance(URI.create(getRequestUri(request)));
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleAllUncaughtException(Exception ex, WebRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    problemDetail.setType(URI.create("https://api.risklens.com/errors/500"));
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setInstance(URI.create(getRequestUri(request)));
    problemDetail.setProperty("timestamp", OffsetDateTime.now());

    return problemDetail;
  }

  private String getRequestUri(WebRequest request) {
    if (request instanceof ServletWebRequest servletWebRequest) {
      return servletWebRequest.getRequest().getRequestURI();
    }
    return request.getDescription(false).replace("uri=", "");
  }

  public record ValidationError(String field, String message) {}
}
