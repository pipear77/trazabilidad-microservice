package com.pragma.trazabilidad.model.traceability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Traceability {
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