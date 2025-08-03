package com.orderingsystem.orderingsystem.service;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;

import java.util.List;

public interface TablesService {
    TablesResponse createTable(TablesRequest request);
    TablesResponse updateTable(Integer id, TablesRequest request);
    TablesResponse softDeleteTable(Integer id);
    void deleteTable(Integer id);
    TablesResponse getTableById(Integer id);
    List<TablesResponse> getAllTables();
}
