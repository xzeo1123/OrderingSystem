package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.entity.Receipts;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.ReceiptDetailsMapper;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptDetailsRepository;
import com.orderingsystem.orderingsystem.repository.ReceiptsRepository;
import com.orderingsystem.orderingsystem.service.ReceiptDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptDetailsServiceImpl implements ReceiptDetailsService {

    private final ReceiptDetailsRepository receiptDetailsRepository;
    private final ReceiptsRepository receiptsRepository;
    private final ProductsRepository productsRepository;
    private final ReceiptDetailsMapper receiptDetailsMapper;

    /* ---------- CREATE ---------- */
    @Override
    public ReceiptDetailsResponse createReceiptDetail(ReceiptDetailsRequest request) {
        Receipts receipt = receiptsRepository.findById(request.getReceiptId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt with id " + request.getReceiptId() + " not found"));

        Products product = productsRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        validate(request, product);

        ReceiptDetails receiptDetail = receiptDetailsMapper.toEntity(request, receipt, product);
        ReceiptDetails savedReceiptDetail = receiptDetailsRepository.save(receiptDetail);

        product.setQuantity(product.getQuantity() + receiptDetail.getQuantity());
        productsRepository.save(product);

        return receiptDetailsMapper.toResponse(savedReceiptDetail);
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteReceiptDetail(Integer id) {
        if (!receiptDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Receipt Detail with id " + id + " not found");
        }
        receiptDetailsRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public ReceiptDetailsResponse getReceiptDetailById(Integer id) {
        ReceiptDetails receiptDetail = receiptDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt Detail with id " + id + " not found"));
        return receiptDetailsMapper.toResponse(receiptDetail);
    }

    @Override
    public List<ReceiptDetailsResponse> getAllReceiptDetails() {
        return receiptDetailsRepository.findAll().stream()
                .map(receiptDetailsMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(ReceiptDetailsRequest request, Products product) {
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new BusinessRuleException("Quantity must be at least 1");
        }
        if (request.getReceiptId() == null || request.getProductId() == null) {
            throw new BusinessRuleException("Receipt ID and Product ID must not be null");
        }
        if (product.getId() == 1) {
            throw new RuntimeException("Cannot interact with this product (ID = 1)");
        }
    }
}
