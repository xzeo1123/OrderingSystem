package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;

import java.util.List;

public interface ReceiptsService {
    ReceiptsResponse createReceipt(ReceiptsRequest request);
    ReceiptsResponse updateReceipt(Integer id, ReceiptsRequest request);
    ReceiptsResponse softDeleteBill(Integer id);
    void deleteReceipt(Integer id);
    ReceiptsResponse getReceiptById(Integer id);
    List<ReceiptsResponse> getAllReceipts();
}
