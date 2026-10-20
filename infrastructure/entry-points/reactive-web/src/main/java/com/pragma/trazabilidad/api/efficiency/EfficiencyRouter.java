package com.pragma.trazabilidad.api.efficiency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EfficiencyRouter {

    @Bean
    public RouterFunction<ServerResponse> efficiencyRoutes(EfficiencyHandler handler) {
        return route(GET("/api/v1/traceability/efficiency/orders"), handler::getOrdersEfficiency)
                .andRoute(GET("/api/v1/traceability/efficiency/ranking"), handler::getEmployeesRanking);
    }
}