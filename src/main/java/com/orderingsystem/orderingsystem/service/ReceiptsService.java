package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ReceiptsService {
    ReceiptsResponse createReceipt(ReceiptsRequest receiptsRequest);
    ReceiptsResponse updateReceipt(Integer receiptId, ReceiptsRequest receiptsRequest);
    ReceiptsResponse softDeleteBill(Integer receiptId);
    void deleteReceipt(Integer id);
    ReceiptsResponse getReceiptById(Integer receiptId);
    Page<ReceiptsResponse> getAllReceipts(Integer page, Integer size);
    List<ReceiptsResponse> getReceiptsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<ReceiptsResponse> getReceiptsByTotalRange(Double minTotal, Double maxTotal);
}
