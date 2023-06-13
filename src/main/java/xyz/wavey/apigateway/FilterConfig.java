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
                .route(p ->
                        p.path("/rental/**", "/insurance/**", "/purchase/**")
                                .filters(f -> f.filters(authorizationHeaderFilter.apply(config -> {})))
                                .uri("lb://RENTAL-SERVICE"))
                // user-service
                .route(p -> p.path("/auth/**").uri("lb://USER-SERVICE"))
                // vehicle-service
                .route(p ->
                        p.path("/booklist/**")
                                .filters(f -> f.filters(authorizationHeaderFilter.apply(config -> {})))
                                .uri("lb://VEHICLE-SERVICE"))
                .route(p ->
                        p.path("/vehicle/**", "/frame/**", "/billitazone/**", "/review/**", "/carbrand/**")
                                .uri("lb://VEHICLE-SERVICE"))
                // board-service
                .route(p -> p.path("/blog/**", "/category/**", "/comment/**", "/like/**", "/reply/**", "/tag/**", "/tag-list/**")
                        .uri("lb://BOARD-SERVICE"))
                .build();

    }
}
