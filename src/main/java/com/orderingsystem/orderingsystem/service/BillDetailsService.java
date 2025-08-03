package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.BillDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;

import java.util.List;

public interface BillDetailsService {
    BillDetailsResponse createBillDetail(BillDetailsRequest request);
//    BillDetailsResponse updateBillDetail(Integer id, BillDetailsRequest request);
    void deleteBillDetail(Integer id);
    BillDetailsResponse getBillDetailById(Integer id);
    List<BillDetailsResponse> getAllBillDetails();
}
