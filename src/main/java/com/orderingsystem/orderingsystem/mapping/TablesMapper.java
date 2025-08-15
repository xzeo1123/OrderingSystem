package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.TablesRequest;
import com.orderingsystem.orderingsystem.dto.response.TablesResponse;
import com.orderingsystem.orderingsystem.entity.Tables;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TablesMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bills", ignore = true)
    Tables toEntity(TablesRequest request);

    TablesResponse toResponse(Tables entity);
}
