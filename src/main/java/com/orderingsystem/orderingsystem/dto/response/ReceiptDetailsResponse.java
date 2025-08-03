package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailsResponse {
    private Integer id;
    private Integer receiptId;
    private Integer productId;
    private Integer quantity;
}
