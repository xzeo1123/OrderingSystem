package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;

import java.util.List;

public interface BillsService {
    BillsResponse createBill(BillsRequest request);
    BillsResponse updateBill(Integer id, BillsRequest request);
    BillsResponse softDeleteBill(Integer id);
    void deleteBill(Integer id);
    BillsResponse getBillById(Integer id);
    List<BillsResponse> getAllBills();
}
