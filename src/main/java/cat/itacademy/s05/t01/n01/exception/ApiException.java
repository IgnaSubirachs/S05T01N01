package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus statusCode;

    public ApiException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

