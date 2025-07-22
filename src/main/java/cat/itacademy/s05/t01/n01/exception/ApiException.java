package cat.itacademy.s05.t01.n01.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public ApiException(ErrorCode errorCode, HttpStatus status) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = status;
    }
}
