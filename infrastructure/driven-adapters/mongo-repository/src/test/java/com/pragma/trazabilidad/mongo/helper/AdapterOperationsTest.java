package com.pragma.trazabilidad.mongo.helper;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.mongo.MongoDBRepository;
import com.pragma.trazabilidad.mongo.MongoRepositoryAdapter;
import com.pragma.trazabilidad.mongo.document.TraceabilityDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    @Mock
    private MongoDBRepository repository;

    private MongoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Instanciamos nuestro adaptador inyectando solo el repositorio mockeado
        adapter = new MongoRepositoryAdapter(repository);
    }

    @Test
    void saveTraceability_Success() {
        // Arrange
        Traceability traceability = Traceability.builder().orderId("pedido-123").clientId(7L).build();
        TraceabilityDocument document = TraceabilityDocument.builder().orderId("pedido-123").clientId(7L).build();

        when(repository.save(any(TraceabilityDocument.class))).thenReturn(Mono.just(document));

        // Act & Assert (Modo Reactivo)
        StepVerifier.create(adapter.saveTraceability(traceability))
                .expectNextMatches(saved -> saved.getOrderId().equals("pedido-123") && saved.getClientId().equals(7L))
                .verifyComplete();
    }

    @Test
    void getTraceabilityByOrderId_Success() {
        // Arrange
        TraceabilityDocument document = TraceabilityDocument.builder().orderId("pedido-123").clientId(7L).build();

        when(repository.findByOrderId("pedido-123")).thenReturn(Flux.just(document));

        // Act & Assert (Modo Reactivo)
        StepVerifier.create(adapter.getTraceabilityByOrderId("pedido-123"))
                .expectNextMatches(found -> found.getOrderId().equals("pedido-123") && found.getClientId().equals(7L))
                .verifyComplete();
    }
}