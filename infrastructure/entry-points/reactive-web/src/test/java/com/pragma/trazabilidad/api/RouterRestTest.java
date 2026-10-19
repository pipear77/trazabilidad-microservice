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

class RouterRestTest {

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
    void saveLog_ShouldReturnCreated() {
        Traceability trace = Traceability.builder().id("1").orderId("ord-123").clientId(7L).newState("PENDIENTE").build();
        when(traceabilityUseCase.saveLog(any(Traceability.class))).thenReturn(Mono.just(trace));

        webTestClient.post()
                .uri("/api/v1/traceability")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(trace)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo("ord-123");
    }

    @Test
    void getTraceability_ShouldReturnOk() {
        Traceability trace = Traceability.builder().id("1").orderId("ord-123").clientId(7L).newState("PENDIENTE").build();
        when(traceabilityUseCase.getTraceability(anyString(), anyLong())).thenReturn(Flux.just(trace));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/traceability/ord-123").queryParam("clientId", "7").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].orderId").isEqualTo("ord-123");
    }

    @Test
    void getTraceability_WhenError_ShouldReturnBadRequest() {
        when(traceabilityUseCase.getTraceability(anyString(), anyLong()))
                .thenReturn(Flux.error(new IllegalArgumentException("No se encontraron registros o el pedido no te pertenece.")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/traceability/ord-123").queryParam("clientId", "7").build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("No se encontraron registros o el pedido no te pertenece.");
    }
}