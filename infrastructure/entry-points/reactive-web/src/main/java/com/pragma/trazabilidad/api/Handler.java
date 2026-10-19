package com.pragma.trazabilidad.api;

import com.pragma.trazabilidad.model.traceability.Traceability;
import com.pragma.trazabilidad.usecase.traceability.TraceabilityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final TraceabilityUseCase traceabilityUseCase;

    // 1. Endpoint POST: Recibe el log de cambio de estado desde pedidos-microservice
    public Mono<ServerResponse> saveLog(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Traceability.class)
                .flatMap(traceabilityUseCase::saveLog)
                .flatMap(savedLog -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedLog));
    }

    // 2. Endpoint GET (HU17): Consulta el historial de un pedido para un cliente
    public Mono<ServerResponse> getTraceability(ServerRequest serverRequest) {
        String orderId = serverRequest.pathVariable("orderId");
        // Por ahora recibimos el clientId por queryParam (?clientId=7) o luego del JWT reactivo
        String clientIdParam = serverRequest.queryParam("clientId").orElse("0");
        Long clientId = Long.parseLong(clientIdParam);

        return traceabilityUseCase.getTraceability(orderId, clientId)
                .collectList()
                .flatMap(logs -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(logs))
                .onErrorResume(IllegalArgumentException.class, ex ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("mensaje", ex.getMessage()))
                );
    }
}