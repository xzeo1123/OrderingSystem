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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public BillsResponse createBill(BillsRequest billsRequest) {
        Users user = usersRepository.findById(billsRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + billsRequest.getUserId() + " not found"));

        Tables table = tablesRepository.findById(billsRequest.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + billsRequest.getTableId() + " not found"));

        billsRequest.setDateCreate(LocalDateTime.now());
        billsRequest.setStatus(Status.ACTIVE);

        Bills bill = billsMapper.toEntity(billsRequest);
        bill.setUser(user);
        bill.setTable(table);

        Bills savedBill = billsRepository.save(bill);

        if (billsRequest.getUserId()!=1) {
            int pointToAdd = savedBill.getTotal().multiply(BigDecimal.valueOf(0.1)).intValue();
            user.setPoint(user.getPoint() + pointToAdd);
            usersRepository.save(user);
        }

        return billsMapper.toResponse(savedBill);
    }

    /* ---------- UPDATE ---------- */
    @Override
    public BillsResponse updateBill(Integer id, BillsRequest billsRequest) {
        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + id + " not found"));

        Users user = usersRepository.findById(billsRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + billsRequest.getUserId() + " not found"));

        Tables table = tablesRepository.findById(billsRequest.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + billsRequest.getTableId() + " not found"));

        Bills updatedBill = billsMapper.toEntity(billsRequest);
        updatedBill.setUser(user);
        updatedBill.setTable(table);

        return billsMapper.toResponse(billsRepository.save(bill));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public BillsResponse softDeleteBill(Integer billid) {
        Bills bill = billsRepository.findById(billid)
                .orElseThrow(() -> new ResourceNotFoundException("Bill " + billid + " not found"));

        bill.setStatus(Status.INACTIVE);

        return billsMapper.toResponse(billsRepository.save(bill));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteBill(Integer billid) {
        if (!billsRepository.existsById(billid)) {
            throw new ResourceNotFoundException("Bill with id " + billid + " not found");
        }

        billsRepository.deleteById(billid);
    }

    /* ---------- READ ---------- */
    @Override
    public BillsResponse getBillById(Integer billid) {
        Bills bill = billsRepository.findById(billid)
                .orElseThrow(() -> new ResourceNotFoundException("Bill with id " + billid + " not found"));
        return billsMapper.toResponse(bill);
    }

    @Override
    public Page<BillsResponse> getAllBills(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return billsRepository.findAll(pageable)
                .map(billsMapper::toResponse);
    }

    @Override
    public List<BillsResponse> getBillsByUser(Integer userId) {
        return billsRepository.findByUser_Id(userId)
                .stream()
                .map(billsMapper::toResponse)
                .toList();
    }

    @Override
    public List<BillsResponse> getBillsByTable(Integer tableId) {
        return billsRepository.findByTable_Id(tableId)
                .stream()
                .map(billsMapper::toResponse)
                .toList();
    }

    @Override
    public List<BillsResponse> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return billsRepository.findByDateCreateBetween(startDate, endDate)
                .stream()
                .map(billsMapper::toResponse)
                .toList();
    }

    @Override
    public List<BillsResponse> getBillsByTotalRange(Double minTotal, Double maxTotal) {
        return billsRepository.findByTotalBetween(minTotal, maxTotal)
                .stream()
                .map(billsMapper::toResponse)
                .toList();
    }

}
