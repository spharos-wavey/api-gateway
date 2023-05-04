package xyz.wavey.apigateway;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${spring.security.oauth.kakao.iss}")
    private String targetIss;

    @Value("${spring.security.oauth.kakao.aud}")
    private String targetAud;

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String[] jwtSplit = jwt.split("[.]");
        String header = jwtSplit[0];
        String payload = jwtSplit[1];
        String Signature = jwtSplit[2];

        if (!isPayloadValid(payload) || !isSignatureValid(header, Signature)) {
            returnValue = false;
        }
        return returnValue;
    }

    private boolean isPayloadValid(String payload) {
        boolean returnValue = true;

        try {
            Base64.Decoder decoder = Base64.getDecoder();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new String(decoder.decode(payload.getBytes())));

            if (!element.getAsJsonObject().get("iss").getAsString().equals(targetIss) ||
                    !element.getAsJsonObject().get("aud").getAsString().equals(targetAud) ||
                    !(Integer.parseInt(element.getAsJsonObject().get("exp").getAsString()) > (System.currentTimeMillis() / 1000))) {
                returnValue = false;
            }
        } catch (Exception e) {
            returnValue = false;
        }

        return returnValue;
    }

    private boolean isSignatureValid(String header, String signature) {
        boolean returnValue = true;

        //todo 서명검증

        return returnValue;
    }

    public static class Config {

    }

}
