package com.orderingsystem.orderingsystem.dto.request;

import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductsRequest {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotBlank(message = "Product image URL cannot be empty")
    private String imageUrl;

    @NotBlank(message = "Product image ID cannot be empty")
    private String imageId;

    @NotNull(message = "Import price cannot be empty")
    @Min(value = 0, message = "Import price must be greater than or equal to 0")
    private BigDecimal importPrice;

    @NotNull(message = "Sale price cannot be empty")
    @Min(value = 0, message = "Sale price must be greater than or equal to 0")
    private BigDecimal salePrice;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    private Status status;

    @NotNull(message = "Category id cannot be empty")
    private Integer categoryId;
}
