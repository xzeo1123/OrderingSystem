package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.BillDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;
import com.orderingsystem.orderingsystem.entity.BillDetails;
import com.orderingsystem.orderingsystem.entity.Bills;
import com.orderingsystem.orderingsystem.entity.Products;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.BillDetailsMapper;
import com.orderingsystem.orderingsystem.repository.BillDetailsRepository;
import com.orderingsystem.orderingsystem.repository.BillsRepository;
import com.orderingsystem.orderingsystem.repository.ProductsRepository;
import com.orderingsystem.orderingsystem.service.BillDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BillDetailsServiceImpl implements BillDetailsService {

    private final BillDetailsRepository billDetailsRepository;
    private final BillsRepository billsRepository;
    private final ProductsRepository productsRepository;
    private final BillDetailsMapper billDetailsMapper;

    /* ---------- CREATE ---------- */
    @Override
    public BillDetailsResponse createBillDetail(BillDetailsRequest billDetailsRequest) {
        Bills bill = billsRepository.findById(billDetailsRequest.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bill with id " + billDetailsRequest.getBillId() + " not found"));

        Products product = productsRepository.findById(billDetailsRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + billDetailsRequest.getProductId() + " not found"));

        validate(billDetailsRequest, product);

        BillDetails billDetail = billDetailsMapper.toEntity(billDetailsRequest);
        billDetail.setBill(bill);
        billDetail.setProduct(product);

        product.setQuantity(product.getQuantity() - billDetail.getQuantity());
        productsRepository.save(product);

        BillDetails savedBillDetails = billDetailsRepository.save(billDetail);

        return billDetailsMapper.toResponse(savedBillDetails);
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteBillDetail(Integer billDetailId) {
        if (!billDetailsRepository.existsById(billDetailId)) {
            throw new ResourceNotFoundException("BillDetail " + billDetailId + " not found");
        }
        billDetailsRepository.deleteById(billDetailId);
    }

    /* ---------- READ ---------- */
    @Override
    public BillDetailsResponse getBillDetailById(Integer billDetailId) {
        BillDetails billDetail = billDetailsRepository.findById(billDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("BillDetail " + billDetailId + " not found"));
        return billDetailsMapper.toResponse(billDetail);
    }

    @Override
    public List<BillDetailsResponse> getAllBillDetails() {
        return billDetailsRepository.findAll()
                .stream()
                .map(billDetailsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillDetailsResponse> getBillDetailsByBill(Integer billId) {
        return billDetailsRepository.findByBill_Id(billId)
                .stream()
                .map(billDetailsMapper::toResponse)
                .toList();
    }

    @Override
    public List<BillDetailsResponse> getBillDetailsByProduct(Integer productId) {
        return billDetailsRepository.findByProduct_Id(productId)
                .stream()
                .map(billDetailsMapper::toResponse)
                .toList();
    }

    /* ---------- VALIDATION ---------- */
    private void validate(BillDetailsRequest billDetailsRequest, Products product) {
        if (billDetailsRequest.getQuantity() > product.getQuantity()) {
            throw new BusinessRuleException("Not enough product quantity in stock");
        }
    }
}
