package com.pragma.trazabilidad.usecase.traceability;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.model.traceability.gateways.TraceabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TraceabilityUseCaseTest {

    @Mock
    private TraceabilityRepository repository;

    @InjectMocks
    private TraceabilityUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLog_Success() {
        Traceability log = Traceability.builder().orderId("ord-1").clientId(7L).newState("PENDIENTE").build();
        when(repository.saveTraceability(any(Traceability.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.saveLog(log))
                .expectNextMatches(saved -> saved.getOrderId().equals("ord-1") && saved.getTimestamp() != null)
                .verifyComplete();
    }

    @Test
    void getTraceability_Success() {
        Traceability log = Traceability.builder().orderId("ord-1").clientId(7L).newState("PENDIENTE").build();
        when(repository.getTraceabilityByOrderId("ord-1")).thenReturn(Flux.just(log));

        StepVerifier.create(useCase.getTraceability("ord-1", 7L))
                .expectNext(log)
                .verifyComplete();
    }

    @Test
    void getTraceability_WrongClient_ShouldReturnError() {
        Traceability log = Traceability.builder().orderId("ord-1").clientId(99L).newState("PENDIENTE").build();
        when(repository.getTraceabilityByOrderId("ord-1")).thenReturn(Flux.just(log));

        StepVerifier.create(useCase.getTraceability("ord-1", 7L))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("No se encontraron registros o el pedido no te pertenece."))
                .verify();
    }
}