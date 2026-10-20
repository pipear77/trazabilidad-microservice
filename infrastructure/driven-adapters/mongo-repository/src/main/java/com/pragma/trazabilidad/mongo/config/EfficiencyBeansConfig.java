package com.pragma.trazabilidad.mongo.config;

import com.pragma.trazabilidad.model.employeeranking.gateways.EmployeeRankingRepository;
import com.pragma.trazabilidad.model.orderefficiency.gateways.OrderEfficiencyRepository;
import com.pragma.trazabilidad.mongo.adapter.EmployeeRankingMongoAdapter;
import com.pragma.trazabilidad.mongo.adapter.OrderEfficiencyMongoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class EfficiencyBeansConfig {

    @Bean
    public OrderEfficiencyRepository orderEfficiencyRepository(ReactiveMongoTemplate mongoTemplate) {
        return new OrderEfficiencyMongoAdapter(mongoTemplate);
    }

    @Bean
    public EmployeeRankingRepository employeeRankingRepository(ReactiveMongoTemplate mongoTemplate) {
        return new EmployeeRankingMongoAdapter(mongoTemplate);
    }
}