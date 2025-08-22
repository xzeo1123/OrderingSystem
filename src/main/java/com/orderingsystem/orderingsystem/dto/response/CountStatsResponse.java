package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountStatsResponse {
    private Long billCount;
    private Long receiptCount;
}