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
    public BillDetailsResponse createBillDetail(BillDetailsRequest request) {
        Bills bill = billsRepository.findById(request.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bill with id " + request.getBillId() + " not found"));

        Products product = productsRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        validate(request, product);

        BillDetails billDetail = billDetailsMapper.toEntity(request, bill, product);
        BillDetails savedBillDetails = billDetailsRepository.save(billDetail);

        product.setQuantity(product.getQuantity() - billDetail.getQuantity());
        productsRepository.save(product);

        return billDetailsMapper.toResponse(savedBillDetails);
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteBillDetail(Integer id) {
        if (!billDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("BillDetail " + id + " not found");
        }
        billDetailsRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public BillDetailsResponse getBillDetailById(Integer id) {
        BillDetails billDetail = billDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BillDetail " + id + " not found"));
        return billDetailsMapper.toResponse(billDetail);
    }

    @Override
    public List<BillDetailsResponse> getAllBillDetails() {
        return billDetailsRepository.findAll()
                .stream()
                .map(billDetailsMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- VALIDATION ---------- */
    private void validate(BillDetailsRequest request, Products product) {
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new BusinessRuleException("Quantity cannot be lower than 1");
        }
        if (request.getQuantity() > product.getQuantity()) {
            throw new BusinessRuleException("Not enough product quantity in stock");
        }
        if (product.getId() == 1) {
            throw new RuntimeException("Cannot interact with this product (ID = 1)");
        }
    }
}
