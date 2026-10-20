package com.pragma.trazabilidad.mongo.adapter;

import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
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
class OrderEfficiencyMongoAdapterTest {

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private OrderEfficiencyMongoAdapter adapter;

    @Test
    void getOrdersEfficiency_ShouldReturnFlux() {
        OrderEfficiency mockEfficiency = new OrderEfficiency("order123", 45L);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("traceability_logs"), eq(OrderEfficiency.class)))
                .thenReturn(Flux.just(mockEfficiency));

        Flux<OrderEfficiency> result = adapter.getOrdersEfficiency();

        StepVerifier.create(result)
                .expectNext(mockEfficiency)
                .verifyComplete();

        verify(mongoTemplate).aggregate(any(Aggregation.class), eq("traceability_logs"), eq(OrderEfficiency.class));
    }
}