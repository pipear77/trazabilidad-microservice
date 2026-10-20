package com.pragma.trazabilidad.model.employeeranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmployeeRanking {
    private Long employeeId;
    private Double averageTimeInMinutes;
}