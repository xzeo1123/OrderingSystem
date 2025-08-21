package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillsService {
    BillsResponse createBill(BillsRequest request);
    BillsResponse updateBill(Integer id, BillsRequest request);
    BillsResponse softDeleteBill(Integer id);
    void deleteBill(Integer id);
    BillsResponse getBillById(Integer id);
    Page<BillsResponse> getAllBills(Integer page, Integer size);
}
