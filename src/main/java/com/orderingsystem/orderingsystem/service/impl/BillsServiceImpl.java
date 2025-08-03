package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.BillDetailsRepository;
import com.orderingsystem.orderingsystem.repository.BillsRepository;
import com.orderingsystem.orderingsystem.repository.TablesRepository;
import com.orderingsystem.orderingsystem.repository.UsersRepository;
import com.orderingsystem.orderingsystem.service.BillsService;
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
public class BillsServiceImpl implements BillsService {

    private final BillsRepository billsRepository;
    private final UsersRepository usersRepository;
    private final TablesRepository tablesRepository;
    private final BillDetailsRepository billDetailsRepository;

    /* ---------- CREATE ---------- */
    @Override
    public BillsResponse createBill(BillsRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserId() + " not found"));

        Tables table = tablesRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + request.getTableId() + " not found"));

        request.setDateCreate(LocalDateTime.now());
        request.setStatus((byte) 1);

        validate(request);

        Bills bill = new Bills();
        bill.setTotal(request.getTotal());
        bill.setDateCreate(request.getDateCreate());
        bill.setNote(request.getNote());
        bill.setStatus((request.getStatus()));
        bill.setUser(user);
        bill.setTable(table);

        Bills savedBill = billsRepository.save(bill);

        if (!user.getId().equals(1)) {
            int pointToAdd = (int) (request.getTotal() * 0.1);
            user.setPoint(user.getPoint() + pointToAdd);
            usersRepository.save(user);
        }

        return toResponse(savedBill);
    }

    /* ---------- UPDATE ---------- */
    @Override
    public BillsResponse updateBill(Integer id, BillsRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this bill (ID = 1)");
        }

        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found"));

        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserId() + " not found"));

        Tables table = tablesRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + request.getTableId() + " not found"));

        validate(request);

        bill.setTotal(request.getTotal());
        bill.setDateCreate(request.getDateCreate());
        bill.setNote(request.getNote());
        bill.setStatus(request.getStatus());
        bill.setUser(user);
        bill.setTable(table);

        return toResponse(billsRepository.save(bill));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public BillsResponse softDeleteBill(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this bill (ID = 1)");
        }

        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill " + id + " not found"));

        bill.setStatus((byte)0);

        return toResponse(billsRepository.save(bill));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteBill(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this bill (ID = 1)");
        }

        if (!billsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found");
        }

        List<BillDetails> billDetailsList = billDetailsRepository.findByProduct_Id(id);
        for (BillDetails bd : billDetailsList) {
            bd.setBill(billsRepository.getReferenceById(1));
        }
        billDetailsRepository.saveAll(billDetailsList);

        billsRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public BillsResponse getBillById(Integer id) {
        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found"));
        return toResponse(bill);
    }

    @Override
    public List<BillsResponse> getAllBills() {
        return billsRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(BillsRequest request) {
        if (request.getTotal() == null || request.getTotal().compareTo(BigDecimal.ZERO.floatValue()) < 0) {
            throw new BusinessRuleException("Total must be a non-negative value");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (unactive) or 1 (active)");
        }
    }

    private BillsResponse toResponse(Bills bill) {
        return BillsResponse.builder()
                .id(bill.getId())
                .total(bill.getTotal())
                .dateCreate(bill.getDateCreate())
                .note(bill.getNote())
                .status(bill.getStatus())
                .userId(bill.getUser().getId())
                .tableId(bill.getTable().getId())
                .build();
    }
}
