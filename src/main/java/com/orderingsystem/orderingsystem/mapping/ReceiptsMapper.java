package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.ReceiptsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptsResponse;
import com.orderingsystem.orderingsystem.entity.Receipts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "details", ignore = true)
    Receipts toEntity(ReceiptsRequest request);

    ReceiptsResponse toResponse(Receipts entity);
}
