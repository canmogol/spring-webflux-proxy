package com.canmogol.webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.web.reactive.function.client.WebClient;

@TypeHint(typeNames = "org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory", methods = @MethodHint(name = "<init>"))
@TypeHint(typeNames = "org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory$Config", methods = @MethodHint(name = "<init>"))
@Configuration
public class ProxyAppConfiguration {

    @Value("${proxy.host:http://centos:3000/}")
    private String proxyHost;

    @Bean
    public WebClient webClient() {
        return WebClient.create(proxyHost);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder, CityGatewayFilterFactory cityGatewayFilterFactory) {
        return routeLocatorBuilder.routes()
                .route("city_path",
                        p -> p.path("/**")
                                .filters(f -> f.filter(cityGatewayFilterFactory.apply(new CityGatewayFilterFactory.Config())))
                                .uri("http://localhost:8080"))
                .build();
    }
}
