package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;

import java.util.List;

public interface ReceiptDetailsService {
    ReceiptDetailsResponse createReceiptDetail(ReceiptDetailsRequest receiptDetailsRequest);
    void deleteReceiptDetail(Integer receiptId);
    ReceiptDetailsResponse getReceiptDetailById(Integer receiptId);
    List<ReceiptDetailsResponse> getAllReceiptDetails();
    List<ReceiptDetailsResponse> getReceiptDetailsByReceipt(Integer receiptId);
    List<ReceiptDetailsResponse> getReceiptDetailsByProduct(Integer productId);
}
