package xyz.wavey.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN("토큰이 유효하지 않습니다", BAD_REQUEST, 400),
    MISSING_AUTH_TOKEN("토큰이 존재하지 않습니다.", UNAUTHORIZED, 401)
    ;
    private final String message;
    private final HttpStatus httpStatus;
    private final Integer errorCode;
}
