package com.pragma.trazabilidad.api;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.usecase.traceability.TraceabilityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HandlerTest {

    private WebTestClient webTestClient;
    private TraceabilityUseCase traceabilityUseCase;

    @BeforeEach
    void setUp() {
        traceabilityUseCase = Mockito.mock(TraceabilityUseCase.class);
        Handler handler = new Handler(traceabilityUseCase);
        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(handler)).build();
    }

    @Test
    void saveLog_Success() {
        Traceability traceability = Traceability.builder().orderId("ord-999").clientId(7L).newState("PENDIENTE").build();
        when(traceabilityUseCase.saveLog(any(Traceability.class))).thenReturn(Mono.just(traceability));

        webTestClient.post()
                .uri("/api/v1/traceability")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(traceability)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo("ord-999");
    }

    @Test
    void getTraceability_Success() {
        Traceability traceability = Traceability.builder().orderId("ord-999").clientId(7L).newState("PENDIENTE").build();
        when(traceabilityUseCase.getTraceability(anyString(), anyLong())).thenReturn(Flux.just(traceability));

        webTestClient.get()
                .uri("/api/v1/traceability/ord-999?clientId=7")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].orderId").isEqualTo("ord-999");
    }

    @Test
    void getTraceability_BadRequest() {
        when(traceabilityUseCase.getTraceability(anyString(), anyLong()))
                .thenReturn(Flux.error(new IllegalArgumentException("No se encontraron registros o el pedido no te pertenece.")));

        webTestClient.get()
                .uri("/api/v1/traceability/ord-999?clientId=7")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("No se encontraron registros o el pedido no te pertenece.");
    }
}