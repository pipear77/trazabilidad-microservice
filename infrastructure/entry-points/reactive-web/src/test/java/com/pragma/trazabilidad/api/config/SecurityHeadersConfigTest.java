package com.pragma.trazabilidad.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNull;

class SecurityHeadersConfigTest {

    @Test
    void filter_ShouldSetSecurityHeadersAndRemoveServerHeader() {
        SecurityHeadersConfig filter = new SecurityHeadersConfig();

        // Simulamos una petición HTTP reactiva
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test").build());

        // Añadimos manualmente el header "Server" para validar que se elimine correctamente
        exchange.getResponse().getHeaders().add("Server", "Tomcat");

        // Mockeamos la cadena de filtros
        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();

        // Validamos la respuesta
        HttpHeaders headers = exchange.getResponse().getHeaders();
        assertEquals("default-src 'self'; frame-ancestors 'self'; form-action 'self'", headers.getFirst("Content-Security-Policy"));
        assertEquals("max-age=31536000; includeSubDomains; preload", headers.getFirst("Strict-Transport-Security"));
        assertEquals("nosniff", headers.getFirst("X-Content-Type-Options"));
        assertEquals("no-store", headers.getFirst("Cache-Control"));
        assertEquals("no-cache", headers.getFirst("Pragma"));
        assertEquals("strict-origin-when-cross-origin", headers.getFirst("Referrer-Policy"));

        // Verificamos que el header Server fue eliminado
        assertNull(headers.getFirst("Server"));

        // Verificamos que se continuó la ejecución de la cadena
        verify(chain).filter(exchange);
    }
}