package com.orderingsystem.orderingsystem.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ProductsRequest {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    private String imageUrl;

    private String imageId;

    @NotNull(message = "Import price cannot be empty")
    private Float importPrice;

    @NotNull(message = "Sale price cannot be empty")
    private Float salePrice;

    private Integer quantity;

    private Byte status;

    @NotNull(message = "Category id cannot be empty")
    private Integer categoryId;
}
