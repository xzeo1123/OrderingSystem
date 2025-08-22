package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillUserStatsResponse {
    private Integer userId;
    private String username;
    private Long billCount;
}