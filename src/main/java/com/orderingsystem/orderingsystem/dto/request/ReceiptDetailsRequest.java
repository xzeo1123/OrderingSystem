package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReceiptDetailsRequest {

    @NotNull(message = "Receipt ID cannot be empty")
    @Min(value = 1, message = "Receipt ID cannot be lower than 1")
    private Integer receiptId;

    @NotNull(message = "Product ID cannot be empty")
    @Min(value = 1, message = "Product ID cannot be lower than 1")
    private Integer productId;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity cannot be lower than 1")
    private Integer quantity;
}
