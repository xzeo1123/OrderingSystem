package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;

import java.util.List;

public interface ReceiptDetailsService {
    ReceiptDetailsResponse createReceiptDetail(ReceiptDetailsRequest request);
    void deleteReceiptDetail(Integer id);
    ReceiptDetailsResponse getReceiptDetailById(Integer id);
    List<ReceiptDetailsResponse> getAllReceiptDetails();
}
