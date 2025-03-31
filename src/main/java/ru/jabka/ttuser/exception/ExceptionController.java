package ru.jabka.ttuser.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.jabka.ttuser.model.ServiceResponse;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ServiceResponse.builder()
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }
}