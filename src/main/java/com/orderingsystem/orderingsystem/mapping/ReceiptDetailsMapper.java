package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.ReceiptDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.ReceiptDetailsResponse;
import com.orderingsystem.orderingsystem.entity.ReceiptDetails;
import com.orderingsystem.orderingsystem.entity.Receipts;
import com.orderingsystem.orderingsystem.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptDetailsMapper {

    @Mapping(source = "receipt.id", target = "receiptId")
    @Mapping(source = "product.id", target = "productId")
    ReceiptDetailsResponse toResponse(ReceiptDetails entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "receipt", target = "receipt")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "request.quantity", target = "quantity")
    ReceiptDetails toEntity(ReceiptDetailsRequest request, Receipts receipt, Products product);
}
