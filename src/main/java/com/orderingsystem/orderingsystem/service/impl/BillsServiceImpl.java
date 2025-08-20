package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.BillsMapper;
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
    private final BillsMapper billsMapper;

    /* ---------- CREATE ---------- */
    @Override
    public BillsResponse createBill(BillsRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserId() + " not found"));

        Tables table = tablesRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + request.getTableId() + " not found"));

        request.setDateCreate(LocalDateTime.now());
        request.setStatus(Status.ACTIVE);

        Bills bill = billsMapper.toEntity(request);
        bill.setUser(user);
        bill.setTable(table);

        Bills savedBill = billsRepository.save(bill);

        if (request.getUserId()!=1) {
            int pointToAdd = savedBill.getTotal().multiply(BigDecimal.valueOf(0.1)).intValue();
            user.setPoint(user.getPoint() + pointToAdd);
            usersRepository.save(user);
        }

        return billsMapper.toResponse(savedBill);
    }

    /* ---------- UPDATE ---------- */
    @Override
    public BillsResponse updateBill(Integer id, BillsRequest request) {
        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found"));

        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + request.getUserId() + " not found"));

        Tables table = tablesRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + request.getTableId() + " not found"));

        Bills updatedBill = billsMapper.toEntity(request);
        updatedBill.setUser(user);
        updatedBill.setTable(table);

        return billsMapper.toResponse(billsRepository.save(bill));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public BillsResponse softDeleteBill(Integer id) {
        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill " + id + " not found"));

        bill.setStatus(Status.INACTIVE);

        return billsMapper.toResponse(billsRepository.save(bill));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteBill(Integer id) {
        if (!billsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill with id " + id + " not found");
        }

        billsRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public BillsResponse getBillById(Integer id) {
        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found"));
        return billsMapper.toResponse(bill);
    }

    @Override
    public List<BillsResponse> getAllBills() {
        return billsRepository.findAll()
                .stream()
                .map(billsMapper::toResponse)
                .collect(Collectors.toList());
    }
}
