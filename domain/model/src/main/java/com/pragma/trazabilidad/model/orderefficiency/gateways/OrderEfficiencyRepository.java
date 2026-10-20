package com.pragma.trazabilidad.model.orderefficiency.gateways;

import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
import reactor.core.publisher.Flux;

public interface OrderEfficiencyRepository {
    Flux<OrderEfficiency> getOrdersEfficiency();
}