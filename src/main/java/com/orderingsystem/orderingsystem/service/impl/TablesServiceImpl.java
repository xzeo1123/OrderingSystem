package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;
import com.orderingsystem.orderingsystem.entity.Bills;
import com.orderingsystem.orderingsystem.entity.Tables;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.BillsRepository;
import com.orderingsystem.orderingsystem.repository.TablesRepository;
import com.orderingsystem.orderingsystem.service.TablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TablesServiceImpl implements TablesService {

    private final TablesRepository tablesRepository;
    private final BillsRepository billsRepository;

    /* ---------- CREATE ---------- */
    @Override
    public TablesResponse createTable(TablesRequest request) {
        request.setStatus((byte)1);

        validate(request);

        if (tablesRepository.existsByNumber(request.getNumber())) {
            throw new BusinessRuleException("Table number already exists");
        }

        Tables table = new Tables();
        table.setNumber(request.getNumber());
        table.setStatus((request.getStatus()));

        return toResponse(tablesRepository.save(table));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public TablesResponse updateTable(Integer id, TablesRequest request) {
        if (id == 1) {
            throw new RuntimeException("Cannot update this table (ID = 1)");
        }

        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + id + " not found"));

        validate(request);

        if (tablesRepository.existsByNumberAndIdNot(request.getNumber(), id)) {
            throw new BusinessRuleException("Table number already exists");
        }

        table.setNumber(request.getNumber());
        table.setStatus(request.getStatus());

        return toResponse(tablesRepository.save(table));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public TablesResponse softDeleteTable(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot soft delete this table (ID = 1)");
        }

        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table " + id + " not found"));

        table.setStatus((byte)0);

        return toResponse(tablesRepository.save(table));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteTable(Integer id) {
        if (id == 1) {
            throw new RuntimeException("Cannot delete this table (ID = 1)");
        }

        if (!tablesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table with id " + id + " not found");
        }

        List<Bills> billsList = billsRepository.findByTable_Id(id);
        for (Bills b : billsList) {
            b.setTable(tablesRepository.getReferenceById(1));
        }
        billsRepository.saveAll(billsList);

        tablesRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public TablesResponse getTableById(Integer id) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + id + " not found"));
        return toResponse(table);
    }

    @Override
    public List<TablesResponse> getAllTables() {
        return tablesRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /* ---------- PRIVATE HELPERS ---------- */
    private void validate(TablesRequest request) {
        if (request.getNumber() == null || request.getNumber() <= 0) {
            throw new BusinessRuleException("Table number must be a positive integer");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessRuleException("Status must be 0 (inactive) or 1 (active)");
        }

    }

    private TablesResponse toResponse(Tables table) {
        return TablesResponse.builder()
                .id(table.getId())
                .number(table.getNumber())
                .status(table.getStatus())
                .build();
    }
}
