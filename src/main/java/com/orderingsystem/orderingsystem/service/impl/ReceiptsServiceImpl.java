package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.entity.Receipts;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptsRepository;
import com.orderingsystem.orderingsystem.service.ReceiptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptsServiceImpl implements ReceiptsService {

    private final ReceiptsRepository receiptsRepository;
    private final ReceiptDetailsRepository receiptDetailsRepository;

    /* ---------- CREATE ---------- */
    @Override
    public ReceiptsResponse createReceipt(ReceiptsRequest request) {
        request.setDateCreate(LocalDateTime.now());
        request.setStatus((byte) 1);

        validate(request);

        Receipts receipt = new Receipts();
        receipt.setTotal(request.getTotal());
        receipt.setDateCreate(request.getDateCreate());
        receipt.setNote(request.getNote());
        receipt.setStatus(request.getStatus());

        return toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ReceiptsResponse updateReceipt(Integer id, ReceiptsRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this receipt (ID = 1)");
        }

        Receipts receipt = receiptsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt with id " + id + " not found"));

        validate(request);

        receipt.setTotal(request.getTotal());
        receipt.setDateCreate(request.getDateCreate());
        receipt.setNote(request.getNote());
        receipt.setStatus(request.getStatus());

        return toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public ReceiptsResponse softDeleteBill(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this receipt (ID = 1)");
        }

        Receipts receipt = receiptsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt " + id + " not found"));

        receipt.setStatus((byte) 0);

        return toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteReceipt(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this receipt (ID = 1)");
        }

        if (!receiptsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Receipt with id " + id + " not found");
        }

        List<ReceiptDetails> receiptDetailsList = receiptDetailsRepository.findByProduct_Id(id);
        for (ReceiptDetails rd : receiptDetailsList) {
            rd.setReceipt(receiptsRepository.getReferenceById(1));
        }
        receiptDetailsRepository.saveAll(receiptDetailsList);

        receiptsRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public ReceiptsResponse getReceiptById(Integer id) {
        Receipts receipt = receiptsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt with id " + id + " not found"));
        return toResponse(receipt);
    }

    @Override
    public List<ReceiptsResponse> getAllReceipts() {
        return receiptsRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(ReceiptsRequest request) {
        if (request.getTotal() == null || request.getTotal().compareTo(BigDecimal.ZERO.floatValue()) < 0) {
            throw new BusinessRuleException("Total must be a non‑negative value");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (inactive) or 1 (active)");
        }
    }

    private ReceiptsResponse toResponse(Receipts receipt) {
        return ReceiptsResponse.builder()
                .id(receipt.getId())
                .total(receipt.getTotal())
                .dateCreate(receipt.getDateCreate())
                .note(receipt.getNote())
                .status(receipt.getStatus())
                .build();
    }
}
