package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.BillDetailsRequest;
import com.orderingsystem.orderingsystem.dto.response.BillDetailsResponse;
import com.orderingsystem.orderingsystem.entity.BillDetails;
import com.orderingsystem.orderingsystem.entity.Bills;
import com.orderingsystem.orderingsystem.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillDetailsMapper {

    @Mapping(source = "bill.id", target = "billId")
    @Mapping(source = "product.id", target = "productId")
    BillDetailsResponse toResponse(BillDetails entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "bill", target = "bill")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "request.quantity", target = "quantity")
    BillDetails toEntity(BillDetailsRequest request, Bills bill, Products product);
}
