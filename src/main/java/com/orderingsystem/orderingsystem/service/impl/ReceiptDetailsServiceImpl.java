package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.entity.Receipts;
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

        ReceiptDetails receiptDetail = receiptDetailsMapper.toEntity(request);
        receiptDetail.setReceipt(receipt);
        receiptDetail.setProduct(product);

        product.setQuantity(product.getQuantity() + receiptDetail.getQuantity());
        productsRepository.save(product);

        ReceiptDetails savedReceiptDetail = receiptDetailsRepository.save(receiptDetail);

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
}
