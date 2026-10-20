package com.pragma.trazabilidad.api.efficiency;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
import com.pragma.trazabilidad.usecase.efficiency.EfficiencyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EfficiencyRouterTest {

    @Mock
    private EfficiencyUseCase efficiencyUseCase;

    @InjectMocks
    private EfficiencyHandler efficiencyHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        EfficiencyRouter router = new EfficiencyRouter();
        // Construimos el WebTestClient enlazado directamente a nuestro Router y Handler sin levantar todo el contexto de Spring
        webTestClient = WebTestClient.bindToRouterFunction(router.efficiencyRoutes(efficiencyHandler)).build();
    }

    @Test
    void getOrdersEfficiency_ShouldReturnOkAndList() {
        OrderEfficiency order1 = new OrderEfficiency("order123", 45L);
        when(efficiencyUseCase.calculateOrdersEfficiency()).thenReturn(Flux.just(order1));

        webTestClient.get()
                .uri("/api/v1/traceability/efficiency/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].orderId").isEqualTo("order123")
                .jsonPath("$[0].elapsedTimeInMinutes").isEqualTo(45);
    }

    @Test
    void getEmployeesRanking_ShouldReturnOkAndList() {
        EmployeeRanking ranking1 = new EmployeeRanking(99L, 15.5);
        when(efficiencyUseCase.getEmployeesRanking()).thenReturn(Flux.just(ranking1));

        webTestClient.get()
                .uri("/api/v1/traceability/efficiency/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].employeeId").isEqualTo(99)
                .jsonPath("$[0].averageTimeInMinutes").isEqualTo(15.5);
    }
}