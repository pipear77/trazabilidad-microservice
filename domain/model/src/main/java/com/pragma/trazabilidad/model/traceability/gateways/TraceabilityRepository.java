package com.pragma.trazabilidad.model.traceability.gateways;

import com.pragma.trazabilidad.model.traceability.Traceability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TraceabilityRepository {
    Mono<Traceability> saveTraceability(Traceability traceability);
    Flux<Traceability> getTraceabilityByOrderId(String orderId);
}