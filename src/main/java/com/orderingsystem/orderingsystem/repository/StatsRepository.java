package com.orderingsystem.orderingsystem.repository;

import com.orderingsystem.orderingsystem.dto.response.BillUserStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.TopProductStatsResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository {
    BigDecimal getRevenueFromBills(LocalDateTime start, LocalDateTime end);
    BigDecimal getRevenueFromReceipts(LocalDateTime start, LocalDateTime end);

    List<TopProductStatsResponse> getTopSellingProducts(LocalDateTime start, LocalDateTime end, int limit);
    List<TopProductStatsResponse> getTopProfitableProducts(LocalDateTime start, LocalDateTime end, int limit);

    Long countBills(LocalDateTime start, LocalDateTime end);
    Long countReceipts(LocalDateTime start, LocalDateTime end);

    List<BillUserStatsResponse> getBillStatsByUser(LocalDateTime start, LocalDateTime end);
}
