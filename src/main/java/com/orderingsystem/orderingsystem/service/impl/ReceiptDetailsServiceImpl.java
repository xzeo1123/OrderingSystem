package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;
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
    public ReceiptDetailsResponse createReceiptDetail(ReceiptDetailsRequest receiptDetailsRequest) {
        Receipts receipt = receiptsRepository.findById(receiptDetailsRequest.getReceiptId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt with id " + receiptDetailsRequest.getReceiptId() + " not found"));

        Products product = productsRepository.findById(receiptDetailsRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + receiptDetailsRequest.getProductId() + " not found"));

        ReceiptDetails receiptDetail = receiptDetailsMapper.toEntity(receiptDetailsRequest);
        receiptDetail.setReceipt(receipt);
        receiptDetail.setProduct(product);

        product.setQuantity(product.getQuantity() + receiptDetail.getQuantity());
        productsRepository.save(product);

        ReceiptDetails savedReceiptDetail = receiptDetailsRepository.save(receiptDetail);

        return receiptDetailsMapper.toResponse(savedReceiptDetail);
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteReceiptDetail(Integer receiptId) {
        if (!receiptDetailsRepository.existsById(receiptId)) {
            throw new ResourceNotFoundException(
                    "Receipt Detail with id " + receiptId + " not found");
        }

        receiptDetailsRepository.deleteById(receiptId);
    }

    /* ---------- READ ---------- */
    @Override
    public ReceiptDetailsResponse getReceiptDetailById(Integer receiptId) {
        ReceiptDetails receiptDetail = receiptDetailsRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt Detail with id " + receiptId + " not found"));

        return receiptDetailsMapper.toResponse(receiptDetail);
    }

    @Override
    public List<ReceiptDetailsResponse> getAllReceiptDetails() {
        return receiptDetailsRepository.findAll().stream()
                .map(receiptDetailsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReceiptDetailsResponse> getReceiptDetailsByReceipt(Integer receiptId) {
        return receiptDetailsRepository.findByReceipt_Id(receiptId)
                .stream()
                .map(receiptDetailsMapper::toResponse)
                .toList();
    }

    @Override
    public List<ReceiptDetailsResponse> getReceiptDetailsByProduct(Integer productId) {
        return receiptDetailsRepository.findByProduct_Id(productId)
                .stream()
                .map(receiptDetailsMapper::toResponse)
                .toList();
    }
}
