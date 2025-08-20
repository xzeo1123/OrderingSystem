package com.orderingsystem.orderingsystem.service.impl;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;
import com.orderingsystem.orderingsystem.entity.Status;
import com.orderingsystem.orderingsystem.entity.Tables;
import com.orderingsystem.orderingsystem.exception.BusinessRuleException;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.mapping.TablesMapper;
import com.orderingsystem.orderingsystem.repository.TablesRepository;
import com.orderingsystem.orderingsystem.service.TablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TablesServiceImpl implements TablesService {

    private final TablesRepository tablesRepository;
    private final TablesMapper tablesMapper;

    /* ---------- CREATE ---------- */
    @Override
    public TablesResponse createTable(TablesRequest request) {
        request.setStatus(Status.ACTIVE);

        validate(request);

        Tables table = tablesMapper.toEntity(request);

        return tablesMapper.toResponse(tablesRepository.save(table));
    }

    /* ---------- UPDATE ---------- */
    @Override
    public TablesResponse updateTable(Integer id, TablesRequest request) {
        Tables existingTable = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + id + " not found"));

        validate(request);

        Tables updatedTable = tablesMapper.toEntity(request);
        updatedTable.setId(existingTable.getId());

        return tablesMapper.toResponse(tablesRepository.save(updatedTable));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    public TablesResponse softDeleteTable(Integer id) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table " + id + " not found"));

        table.setStatus(Status.INACTIVE);

        return tablesMapper.toResponse(tablesRepository.save(table));
    }

    /* ---------- DELETE ---------- */
    @Override
    public void deleteTable(Integer id) {
        if (!tablesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table with id " + id + " not found");
        }

        tablesRepository.deleteById(id);
    }

    /* ---------- READ ---------- */
    @Override
    public TablesResponse getTableById(Integer id) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + id + " not found"));
        return tablesMapper.toResponse(table);
    }

    @Override
    public List<TablesResponse> getAllTables() {
        return tablesRepository.findAll()
                .stream()
                .map(tablesMapper::toResponse)
                .toList();
    }

    /* ---------- VALIDATE ---------- */
    private void validate(TablesRequest request) {
        if (tablesRepository.existsByNumber(request.getNumber())) {
            throw new BusinessRuleException("Table number already exists");
        }
    }
}
