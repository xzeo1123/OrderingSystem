package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponse {
    private Integer id;
    private String name;
    private String imageUrl;
    private Float importPrice;
    private Float salePrice;
    private Integer quantity;
    private Byte status;
    private Integer categoryId;
}
