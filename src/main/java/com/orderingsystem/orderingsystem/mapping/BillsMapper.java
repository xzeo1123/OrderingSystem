package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.BillsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillsResponse;
import com.orderingsystem.orderingsystem.entity.Bills;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillsMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "table.id", target = "tableId")
    BillsResponse toResponse(Bills entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "table", ignore = true)
    @Mapping(target = "details", ignore = true)
    Bills toEntity(BillsRequest request);
}
