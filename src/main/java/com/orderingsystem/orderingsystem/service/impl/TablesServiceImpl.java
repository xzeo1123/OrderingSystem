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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames = "tables")
public class TablesServiceImpl implements TablesService {

    private final TablesRepository tablesRepository;
    private final TablesMapper tablesMapper;

    /* ---------- CREATE ---------- */
    @Override
    @CacheEvict(allEntries = true)
    public TablesResponse createTable(TablesRequest tablesRequest) {
        tablesRequest.setStatus(Status.ACTIVE);

        validate(tablesRequest);

        Tables table = tablesMapper.toEntity(tablesRequest);

        return tablesMapper.toResponse(tablesRepository.save(table));
    }

    /* ---------- UPDATE ---------- */
    @Override
    @CachePut(key = "#tableId")
    @CacheEvict(allEntries = true)
    public TablesResponse updateTable(Integer tableId, TablesRequest tablesRequest) {
        Tables existingTable = tablesRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + tableId + " not found"));

        validate(tablesRequest);

        Tables updatedTable = tablesMapper.toEntity(tablesRequest);
        updatedTable.setId(existingTable.getId());

        return tablesMapper.toResponse(tablesRepository.save(updatedTable));
    }

    /* ---------- SOFT DELETE ---------- */
    @Override
    @CacheEvict(key = "#tableId", allEntries = true)
    public TablesResponse softDeleteTable(Integer tableId) {
        Tables table = tablesRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table " + tableId + " not found"));

        table.setStatus(Status.INACTIVE);

        return tablesMapper.toResponse(tablesRepository.save(table));
    }

    /* ---------- DELETE ---------- */
    @Override
    @CacheEvict(key = "#tableId", allEntries = true)
    public void deleteTable(Integer tableId) {
        if (!tablesRepository.existsById(tableId)) {
            throw new ResourceNotFoundException("Table with id " + tableId + " not found");
        }

        tablesRepository.deleteById(tableId);
    }

    /* ---------- READ ---------- */
    @Override
    @Cacheable(key = "#tableId")
    public TablesResponse getTableById(Integer tableId) {
        Tables table = tablesRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + tableId + " not found"));
        return tablesMapper.toResponse(table);
    }

    @Override
    @Cacheable(value = "categories_list")
    public List<TablesResponse> getAllTables() {
        return tablesRepository.findAll()
                .stream()
                .map(tablesMapper::toResponse)
                .toList();
    }

    /* ---------- VALIDATE ---------- */
    private void validate(TablesRequest tablesRequest) {
        if (tablesRepository.existsByNumber(tablesRequest.getNumber())) {
            throw new BusinessRuleException("Table number already exists");
        }
    }
}
