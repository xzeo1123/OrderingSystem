package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReceiptDetailsRequest {

    @NotNull(message = "Receipt id cannot be empty")
    private Integer receiptId;

    @NotNull(message = "Product id cannot be empty")
    private Integer productId;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
