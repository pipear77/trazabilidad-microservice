package com.pragma.trazabilidad.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "traceability_logs")
public class TraceabilityDocument {
    @Id
    private String id;
    private String orderId;
    private Long clientId;
    private String clientEmail;
    private String previousState;
    private String newState;
    private Long employeeId;
    private String employeeEmail;
    private LocalDateTime timestamp;
}