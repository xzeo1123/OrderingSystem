package com.orderingsystem.orderingsystem.dto.response;

import com.orderingsystem.orderingsystem.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponse {

    private Integer id;

    private String name;

    private String imageUrl;

    private BigDecimal importPrice;

    private BigDecimal salePrice;

    private Integer quantity;

    private Status status;

    private Integer categoryId;
}
