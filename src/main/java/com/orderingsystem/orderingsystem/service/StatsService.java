package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.StatsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillUserStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.CountStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.RevenueStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.TopProductStatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    RevenueStatsResponse getRevenueStats(StatsRequest request);
    List<TopProductStatsResponse> getTopSellingProducts(StatsRequest request);
    List<TopProductStatsResponse> getTopProfitableProducts(StatsRequest request);
    CountStatsResponse getBillReceiptCount(StatsRequest request);
    List<BillUserStatsResponse> getBillStatsByUser(StatsRequest request);
}
