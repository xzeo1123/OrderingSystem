package com.orderingsystem.orderingsystem.controller;

import com.orderingsystem.orderingsystem.dto.request.StatsRequest;
import com.orderingsystem.orderingsystem.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/revenue")
    public ResponseEntity<?> getRevenue(@RequestBody StatsRequest request) {
        return ResponseEntity.ok(statsService.getRevenueStats(request));
    }

    @PostMapping("/top-selling-products")
    public ResponseEntity<?> getTopSellingProducts(@RequestBody StatsRequest request) {
        return ResponseEntity.ok(statsService.getTopSellingProducts(request));
    }

    @PostMapping("/top-profitable-products")
    public ResponseEntity<?> getTopProfitableProducts(@RequestBody StatsRequest request) {
        return ResponseEntity.ok(statsService.getTopProfitableProducts(request));
    }

    @PostMapping("/counts")
    public ResponseEntity<?> getCounts(@RequestBody StatsRequest request) {
        return ResponseEntity.ok(statsService.getBillReceiptCount(request));
    }

    @PostMapping("/bill-by-user")
    public ResponseEntity<?> getBillByUser(@RequestBody StatsRequest request) {
        return ResponseEntity.ok(statsService.getBillStatsByUser(request));
    }
}
