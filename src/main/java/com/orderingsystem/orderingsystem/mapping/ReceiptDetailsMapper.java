package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptDetailsMapper {

    @Mapping(source = "receipt.id", target = "receiptId")
    @Mapping(source = "product.id", target = "productId")
    ReceiptDetailsResponse toResponse(ReceiptDetails entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receipt", ignore = true)
    @Mapping(target = "product", ignore = true)
    ReceiptDetails toEntity(ReceiptDetailsRequest request);
}
