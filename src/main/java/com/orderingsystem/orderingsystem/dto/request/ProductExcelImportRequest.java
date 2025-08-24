package com.orderingsystem.orderingsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductExcelImportRequest {
    private Integer id;
    private String name;
    private BigDecimal importPrice;
    private BigDecimal salePrice;
    private Integer quantity;
    private Integer categoryId;
}
