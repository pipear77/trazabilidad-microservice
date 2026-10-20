package com.pragma.trazabilidad.usecase.efficiency;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import com.pragma.trazabilidad.model.employeeranking.gateways.EmployeeRankingRepository;
import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
import com.pragma.trazabilidad.model.orderefficiency.gateways.OrderEfficiencyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class EfficiencyUseCase {

    private final OrderEfficiencyRepository orderEfficiencyRepository;
    private final EmployeeRankingRepository employeeRankingRepository;

    public Flux<OrderEfficiency> calculateOrdersEfficiency() {
        return orderEfficiencyRepository.getOrdersEfficiency();
    }

    public Flux<EmployeeRanking> getEmployeesRanking() {
        return employeeRankingRepository.getEmployeesRanking();
    }
}