package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.entity.Receipts;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.ReceiptsMapper;
import com.orderingsystem.orderingsystem.repository.ReceiptsRepository;
import com.orderingsystem.orderingsystem.service.ReceiptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptsServiceImpl implements ReceiptsService {

    private final ReceiptsRepository receiptsRepository;
    private final ReceiptsMapper receiptMapper;

    /* ---------- CREATE ---------- */
    @Override
    public ReceiptsResponse createReceipt(ReceiptsRequest receiptsRequest) {
        if (receiptsRequest.getStatus() == null) {
            receiptsRequest.setStatus(Status.ACTIVE);
        }

        Receipts receipt = receiptMapper.toEntity(receiptsRequest);
        return receiptMapper.toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ReceiptsResponse updateReceipt(Integer receiptId, ReceiptsRequest receiptsRequest) {
        Receipts existing = receiptsRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt with id " + receiptId + " not found"));

        Receipts updated = receiptMapper.toEntity(receiptsRequest);
        updated.setId(existing.getId());
        updated.setDateCreate(existing.getDateCreate());
        updated.setDetails(existing.getDetails());

        return receiptMapper.toResponse(receiptsRepository.save(updated));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public ReceiptsResponse softDeleteBill(Integer receiptId) {
        Receipts receipt = receiptsRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt " + receiptId + " not found"));

        receipt.setStatus(Status.INACTIVE);

        return receiptMapper.toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteReceipt(Integer receiptId) {
        if (!receiptsRepository.existsById(receiptId)) {
            throw new ResourceNotFoundException("Receipt with id " + receiptId + " not found");
        }

        receiptsRepository.deleteById(receiptId);
    }

    /* ---------- READ ---------- */
    @Override
    public ReceiptsResponse getReceiptById(Integer receiptId) {
        Receipts receipt = receiptsRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt with id " + receiptId + " not found"));
        return receiptMapper.toResponse(receipt);
    }

    @Override
    public Page<ReceiptsResponse> getAllReceipts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return receiptsRepository.findAll(pageable)
                .map(receiptMapper::toResponse);
    }

    @Override
    public List<ReceiptsResponse> getReceiptsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return receiptsRepository.findByDateCreateBetween(startDate, endDate)
                .stream()
                .map(receiptMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReceiptsResponse> getReceiptsByTotalRange(Double minTotal, Double maxTotal) {
        return receiptsRepository.findByTotalBetween(minTotal, maxTotal)
                .stream()
                .map(receiptMapper::toResponse)
                .toList();
    }
}
