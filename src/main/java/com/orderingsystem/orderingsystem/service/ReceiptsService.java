package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReceiptsService {
    ReceiptsResponse createReceipt(ReceiptsRequest request);
    ReceiptsResponse updateReceipt(Integer id, ReceiptsRequest request);
    ReceiptsResponse softDeleteBill(Integer id);
    void deleteReceipt(Integer id);
    ReceiptsResponse getReceiptById(Integer id);
    Page<ReceiptsResponse> getAllReceipts(Integer page, Integer size);
}
