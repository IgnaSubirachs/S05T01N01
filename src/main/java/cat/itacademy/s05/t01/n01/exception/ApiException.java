package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message, HttpStatus statusCode) {
        super(message);
        this.status = statusCode;
    }
}