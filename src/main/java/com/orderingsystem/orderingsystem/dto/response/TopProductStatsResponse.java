package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductStatsResponse {
    private Integer productId;
    private String productName;
    private Long totalQuantity;
    private Double totalProfit; // dùng cho API lợi nhuận
}