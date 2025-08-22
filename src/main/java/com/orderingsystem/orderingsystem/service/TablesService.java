package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;

import java.util.List;

public interface TablesService {
    TablesResponse createTable(TablesRequest tablesRequest);
    TablesResponse updateTable(Integer id, TablesRequest tablesRequest);
    TablesResponse softDeleteTable(Integer tableId);
    void deleteTable(Integer tableId);
    TablesResponse getTableById(Integer tableId);
    List<TablesResponse> getAllTables();
}
