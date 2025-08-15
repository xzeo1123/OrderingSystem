package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.ProductsRequest;
import com.orderingsystem.orderingsystem.dto.response.ProductsResponse;
import com.orderingsystem.orderingsystem.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "billDetails", ignore = true)
    @Mapping(target = "receiptDetails", ignore = true)
    Products toEntity(ProductsRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    ProductsResponse toResponse(Products products);
}
