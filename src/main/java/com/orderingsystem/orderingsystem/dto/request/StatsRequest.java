package com.orderingsystem.orderingsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer limit;
}