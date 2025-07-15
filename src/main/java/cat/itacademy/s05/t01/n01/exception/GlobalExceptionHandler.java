package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return Mono.just(ResponseEntity
                .badRequest()
                .body(errors));
    }

    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleApiException(ApiException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(ex.getStatus())
                .body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unexpected error: " + ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response));
    }
}
