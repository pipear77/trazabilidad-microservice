package com.pragma.trazabilidad.mongo;

import com.pragma.trazabilidad.mongo.document.TraceabilityDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoDBRepository extends ReactiveMongoRepository<TraceabilityDocument, String> {
    // Spring Data MongoDB Reactivo genera la query automáticamente
    Flux<TraceabilityDocument> findByOrderId(String orderId);
}