package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillDetailsRequest {

    @NotNull(message = "Bill ID cannot be empty")
    @Min(value = 1, message = "Bill ID cannot be lower than 1")
    private Integer billId;

    @NotNull(message = "Product ID cannot be empty")
    @Min(value = 1, message = "Product ID cannot be lower than 1")
    private Integer productId;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity cannot be lower than 1")
    private Integer quantity;
}
