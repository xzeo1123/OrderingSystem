package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.BillDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;

import java.util.List;

public interface BillDetailsService {
    BillDetailsResponse createBillDetail(BillDetailsRequest billDetailsRequest);
    void deleteBillDetail(Integer billId);
    BillDetailsResponse getBillDetailById(Integer billId);
    List<BillDetailsResponse> getAllBillDetails();
    List<BillDetailsResponse> getBillDetailsByBill(Integer billId);
    List<BillDetailsResponse> getBillDetailsByProduct(Integer productId);
}
