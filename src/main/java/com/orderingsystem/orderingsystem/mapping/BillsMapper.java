package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.entity.Bills;
import com.orderingsystem.orderingsystem.entity.Tables;
import com.orderingsystem.orderingsystem.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillsMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "table.id", target = "tableId")
    BillsResponse toResponse(Bills entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user", target = "user")
    @Mapping(source = "table", target = "table")
    @Mapping(source = "request.total", target = "total")
    @Mapping(source = "request.dateCreate", target = "dateCreate")
    @Mapping(source = "request.note", target = "note")
    @Mapping(source = "request.status", target = "status")
    Bills toEntity(BillsRequest request, Users user, Tables table);
}
