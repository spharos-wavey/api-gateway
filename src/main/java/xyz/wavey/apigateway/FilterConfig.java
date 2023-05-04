package xyz.wavey.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder
            , AuthorizationHeaderFilter authorizationHeaderFilter) {
        return builder.routes()
                // rental-service
                .route(p -> p.path("/rental/**", "/insurance/**").uri("lb://RENTAL-SERVICE"))
                // user-service
                .route(p -> p.path("/auth/kakao/**").uri("lb://USER-SERVICE"))
//                .route(p -> p.path("/auth/**")
//                        .filters(f -> f.filters(authorizationHeaderFilter.apply(config -> {})))
//                        .uri("lb://USER-SERVICE"))
                .build();

    }
}
