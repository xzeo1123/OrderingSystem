package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.entity.Receipts;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.ReceiptsMapper;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptsRepository;
import com.orderingsystem.orderingsystem.service.ReceiptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptsServiceImpl implements ReceiptsService {

    private final ReceiptsRepository receiptsRepository;
    private final ReceiptDetailsRepository receiptDetailsRepository;
    private final ReceiptsMapper receiptMapper;

    /* ---------- CREATE ---------- */
    @Override
    public ReceiptsResponse createReceipt(ReceiptsRequest request) {
        if (request.getStatus() == null) {
            request.setStatus(Status.ACTIVE);
        }

        validate(request);

        Receipts receipt = receiptMapper.toEntity(request);
        return receiptMapper.toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public ReceiptsResponse updateReceipt(Integer id, ReceiptsRequest request) {
        Receipts existing = receiptsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt with id " + id + " not found"));

        validate(request);

        Receipts updated = receiptMapper.toEntity(request);
        updated.setId(existing.getId());
        updated.setDateCreate(existing.getDateCreate());
        updated.setDetails(existing.getDetails());

        return receiptMapper.toResponse(receiptsRepository.save(updated));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public ReceiptsResponse softDeleteBill(Integer id) {
        Receipts receipt = receiptsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt " + id + " not found"));

        receipt.setStatus(Status.INACTIVE);

        return receiptMapper.toResponse(receiptsRepository.save(receipt));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteReceipt(Integer id) {
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
        return receiptMapper.toResponse(receipt);
    }

    @Override
    public List<ReceiptsResponse> getAllReceipts() {
        return receiptsRepository.findAll()
                .stream()
                .map(receiptMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(ReceiptsRequest request) {
        if (request.getTotal() == null || request.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Total must be a non-negative value");
        }
    }
}
