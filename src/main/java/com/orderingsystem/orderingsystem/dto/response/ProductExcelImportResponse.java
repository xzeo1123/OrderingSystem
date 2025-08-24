package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductExcelImportResponse {
    private int createdCount;
    private int updatedCount;
    private int totalQuantity;
    private BigDecimal totalAmount;
}
