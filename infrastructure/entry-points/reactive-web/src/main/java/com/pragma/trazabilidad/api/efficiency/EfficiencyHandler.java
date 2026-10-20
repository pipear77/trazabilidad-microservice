package com.pragma.trazabilidad.api.efficiency;

import com.pragma.trazabilidad.usecase.efficiency.EfficiencyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EfficiencyHandler {

    private final EfficiencyUseCase efficiencyUseCase;

    public Mono<ServerResponse> getOrdersEfficiency(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(efficiencyUseCase.calculateOrdersEfficiency(), com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency.class);
    }

    public Mono<ServerResponse> getEmployeesRanking(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(efficiencyUseCase.getEmployeesRanking(), com.pragma.trazabilidad.model.employeeranking.EmployeeRanking.class);
    }
}