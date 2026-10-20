package com.pragma.trazabilidad.usecase.efficiency;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import com.pragma.trazabilidad.model.employeeranking.gateways.EmployeeRankingRepository;
import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
import com.pragma.trazabilidad.model.orderefficiency.gateways.OrderEfficiencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EfficiencyUseCaseTest {

    @Mock
    private OrderEfficiencyRepository orderEfficiencyRepository;

    @Mock
    private EmployeeRankingRepository employeeRankingRepository;

    @InjectMocks
    private EfficiencyUseCase efficiencyUseCase;

    @Test
    void calculateOrdersEfficiency_ShouldReturnFluxOfOrderEfficiency() {
        OrderEfficiency order1 = new OrderEfficiency("order1", 15L);
        OrderEfficiency order2 = new OrderEfficiency("order2", 20L);

        when(orderEfficiencyRepository.getOrdersEfficiency()).thenReturn(Flux.just(order1, order2));

        Flux<OrderEfficiency> result = efficiencyUseCase.calculateOrdersEfficiency();

        StepVerifier.create(result)
                .expectNext(order1)
                .expectNext(order2)
                .verifyComplete();

        verify(orderEfficiencyRepository).getOrdersEfficiency();
    }

    @Test
    void getEmployeesRanking_ShouldReturnFluxOfEmployeeRanking() {
        EmployeeRanking emp1 = new EmployeeRanking(5L, 12.5);
        EmployeeRanking emp2 = new EmployeeRanking(3L, 18.0);

        when(employeeRankingRepository.getEmployeesRanking()).thenReturn(Flux.just(emp1, emp2));

        Flux<EmployeeRanking> result = efficiencyUseCase.getEmployeesRanking();

        StepVerifier.create(result)
                .expectNext(emp1)
                .expectNext(emp2)
                .verifyComplete();

        verify(employeeRankingRepository).getEmployeesRanking();
    }
}