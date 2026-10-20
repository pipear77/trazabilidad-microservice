package com.pragma.trazabilidad.mongo.adapter;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeRankingMongoAdapterTest {

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private EmployeeRankingMongoAdapter adapter;

    @Test
    void getEmployeesRanking_ShouldReturnFlux() {
        EmployeeRanking mockRanking = new EmployeeRanking(5L, 12.5);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("traceability_logs"), eq(EmployeeRanking.class)))
                .thenReturn(Flux.just(mockRanking));

        Flux<EmployeeRanking> result = adapter.getEmployeesRanking();

        StepVerifier.create(result)
                .expectNext(mockRanking)
                .verifyComplete();

        verify(mongoTemplate).aggregate(any(Aggregation.class), eq("traceability_logs"), eq(EmployeeRanking.class));
    }
}