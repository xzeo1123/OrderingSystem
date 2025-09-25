package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatsResponse {
    private BigDecimal totalRevenue;
    private BigDecimal totalImport;
}
