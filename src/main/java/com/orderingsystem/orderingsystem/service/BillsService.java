package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BillsService {
    BillsResponse createBill(BillsRequest billsRequest);
    BillsResponse updateBill(Integer billId, BillsRequest billsRequest);
    BillsResponse softDeleteBill(Integer billId);
    void deleteBill(Integer billId);
    BillsResponse getBillById(Integer billId);
    Page<BillsResponse> getAllBills(Integer page, Integer size);
    List<BillsResponse> getBillsByUser(Integer userId);
    List<BillsResponse> getBillsByTable(Integer tableId);
    List<BillsResponse> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<BillsResponse> getBillsByTotalRange(Double minTotal, Double maxTotal);
}
