package com.pragma.trazabilidad.mongo;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.model.traceability.gateways.TraceabilityRepository;
import com.pragma.trazabilidad.mongo.document.TraceabilityDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements TraceabilityRepository {

    private final MongoDBRepository repository;

    @Override
    public Mono<Traceability> saveTraceability(Traceability traceability) {
        TraceabilityDocument document = toDocument(traceability);
        return repository.save(document)
                .map(this::toModel); // Transforma el Document guardado de vuelta a Model
    }

    @Override
    public Flux<Traceability> getTraceabilityByOrderId(String orderId) {
        return repository.findByOrderId(orderId)
                .map(this::toModel); // Transforma el flujo de Documents a flujo de Models
    }

    // --- Mappers Manuales (Para no depender de librerías extra por ahora) ---
    private TraceabilityDocument toDocument(Traceability model) {
        return TraceabilityDocument.builder()
                .id(model.getId())
                .orderId(model.getOrderId())
                .clientId(model.getClientId())
                .clientEmail(model.getClientEmail())
                .previousState(model.getPreviousState())
                .newState(model.getNewState())
                .employeeId(model.getEmployeeId())
                .employeeEmail(model.getEmployeeEmail())
                .timestamp(model.getTimestamp())
                .build();
    }

    private Traceability toModel(TraceabilityDocument document) {
        return Traceability.builder()
                .id(document.getId())
                .orderId(document.getOrderId())
                .clientId(document.getClientId())
                .clientEmail(document.getClientEmail())
                .previousState(document.getPreviousState())
                .newState(document.getNewState())
                .employeeId(document.getEmployeeId())
                .employeeEmail(document.getEmployeeEmail())
                .timestamp(document.getTimestamp())
                .build();
    }
}