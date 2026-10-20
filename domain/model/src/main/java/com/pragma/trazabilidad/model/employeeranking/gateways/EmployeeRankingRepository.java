package com.pragma.trazabilidad.model.employeeranking.gateways;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import reactor.core.publisher.Flux;

public interface EmployeeRankingRepository {
    Flux<EmployeeRanking> getEmployeesRanking();
}