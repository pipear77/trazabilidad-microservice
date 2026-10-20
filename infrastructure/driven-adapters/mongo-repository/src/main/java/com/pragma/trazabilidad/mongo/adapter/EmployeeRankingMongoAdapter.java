package com.pragma.trazabilidad.mongo.adapter;

import com.pragma.trazabilidad.model.employeeranking.EmployeeRanking;
import com.pragma.trazabilidad.model.employeeranking.gateways.EmployeeRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class EmployeeRankingMongoAdapter implements EmployeeRankingRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<EmployeeRanking> getEmployeesRanking() {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("newState").in("PENDIENTE", "ENTREGADO")),
                group("orderId")
                        .min("timestamp").as("startTime")
                        .max("timestamp").as("endTime")
                        .max("employeeId").as("employeeId"),
                match(Criteria.where("employeeId").ne(null)),
                project("employeeId")
                        .andExpression("(endTime - startTime) / 60000").as("duration"),
                group("employeeId")
                        .avg("duration").as("averageTimeInMinutes"),
                project()
                        .and("_id").as("employeeId")
                        .and("averageTimeInMinutes").as("averageTimeInMinutes"),
                sort(Sort.Direction.ASC, "averageTimeInMinutes")
        );
        return mongoTemplate.aggregate(aggregation, "traceability_logs", EmployeeRanking.class);
    }
}