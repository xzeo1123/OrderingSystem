package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.StatsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillUserStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.CountStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.RevenueStatsResponse;
import com.orderingsystem.orderingsystem.dto.response.TopProductStatsResponse;
import com.orderingsystem.orderingsystem.repository.StatsRepository;
import com.orderingsystem.orderingsystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public RevenueStatsResponse getRevenueStats(StatsRequest request) {
        Double billRevenue = statsRepository.getRevenueFromBills(request.getStartDate(), request.getEndDate());
        Double receiptCost = statsRepository.getRevenueFromReceipts(request.getStartDate(), request.getEndDate());
        return new RevenueStatsResponse(
                billRevenue != null ? billRevenue : 0.0,
                receiptCost != null ? receiptCost : 0.0
        );
    }

    @Override
    public List<TopProductStatsResponse> getTopSellingProducts(StatsRequest request) {
        return statsRepository.getTopSellingProducts(request.getStartDate(), request.getEndDate(),
                request.getLimit() != null ? request.getLimit() : 5);
    }

    @Override
    public List<TopProductStatsResponse> getTopProfitableProducts(StatsRequest request) {
        return statsRepository.getTopProfitableProducts(request.getStartDate(), request.getEndDate(),
                request.getLimit() != null ? request.getLimit() : 5);
    }

    @Override
    public CountStatsResponse getBillReceiptCount(StatsRequest request) {
        Long bills = statsRepository.countBills(request.getStartDate(), request.getEndDate());
        Long receipts = statsRepository.countReceipts(request.getStartDate(), request.getEndDate());
        return new CountStatsResponse(bills, receipts);
    }

    @Override
    public List<BillUserStatsResponse> getBillStatsByUser(StatsRequest request) {
        return statsRepository.getBillStatsByUser(request.getStartDate(), request.getEndDate());
    }
}

