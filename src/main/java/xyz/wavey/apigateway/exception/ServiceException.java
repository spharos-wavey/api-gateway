package xyz.wavey.apigateway.exception;

import lombok.Getter;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Getter
public class ServiceException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
    public ServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
