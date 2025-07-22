package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(WebExchangeBindException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");

        List<Map<String, String>> violations = ex.getFieldErrors().stream()
                .map(error -> {
                    Map<String, String> violation = new HashMap<>();
                    violation.put("field", error.getField());
                    violation.put("message", error.getDefaultMessage());
                    return violation;
                })
                .toList();

        response.put("violations", violations);

        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleApiException(ApiException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getErrorCode().getMessage());
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
