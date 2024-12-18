package com.sivalabs.blog.api;

import com.sivalabs.blog.domain.BadRequestException;
import com.sivalabs.blog.domain.ResourceNotFoundException;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = RestApiExceptionHandler.class)
class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handle(Exception e) {
        var errors = List.of(e.getMessage());
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrors);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiErrors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handle(BadCredentialsException e) {
        var errors = List.of(e.getMessage());
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrors);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handle(BadRequestException e) {
        var errors = List.of(e.getMessage());
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(apiErrors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handle(ResourceNotFoundException e) {
        var errors = List.of(e.getMessage());
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handle(AccessDeniedException e) {
        var errors = List.of(e.getMessage());
        ApiErrors apiErrors = ApiErrors.from(errors);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiErrors);
    }

    record ApiErrors(Errors errors) {
        public static ApiErrors from(List<String> body) {
            return new ApiErrors(new Errors(body));
        }
    }

    record Errors(List<String> body) {}
}
