package com.pragma.trazabilidad.model.orderefficiency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderEfficiency {
    private String orderId;
    private Long elapsedTimeInMinutes;
}