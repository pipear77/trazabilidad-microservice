package com.pragma.trazabilidad.usecase.traceability;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.model.traceability.gateways.TraceabilityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TraceabilityUseCase {

    private final TraceabilityRepository repository;

    // Método para guardar el log (Será consumido internamente o por el otro microservicio)
    public Mono<Traceability> saveLog(Traceability traceability) {
        if (traceability.getTimestamp() == null) {
            traceability.setTimestamp(LocalDateTime.now());
        }
        return repository.saveTraceability(traceability);
    }

    // Método para la HU17: Consultar trazabilidad validando al dueño
    public Flux<Traceability> getTraceability(String orderId, Long clientId) {
        return repository.getTraceabilityByOrderId(orderId)
                // Criterio 2: Filtramos asíncronamente para que solo pasen los del cliente logueado
                .filter(log -> log.getClientId().equals(clientId))
                // Si el flujo queda vacío (no es su pedido o no existe), lanzamos un error reactivo
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontraron registros o el pedido no te pertenece.")));
    }
}