package com.pragma.trazabilidad.mongo.adapter;

import com.pragma.trazabilidad.model.orderefficiency.OrderEfficiency;
import com.pragma.trazabilidad.model.orderefficiency.gateways.OrderEfficiencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class OrderEfficiencyMongoAdapter implements OrderEfficiencyRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<OrderEfficiency> getOrdersEfficiency() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("newState").in("PENDIENTE", "ENTREGADO")),
                group("orderId")
                        .min("timestamp").as("startTime")
                        .max("timestamp").as("endTime"),
                project()
                        .and("_id").as("orderId")
                        .andExpression("(endTime - startTime) / 60000").as("elapsedTimeInMinutes")
        );
        return mongoTemplate.aggregate(aggregation, "traceability_logs", OrderEfficiency.class);
    }
}