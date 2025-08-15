package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.CategoriesRequest;
import com.orderingsystem.orderingsystem.dto.response.CategoriesResponse;
import com.orderingsystem.orderingsystem.entity.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    @Mapping(target = "id", ignore = true)
    Categories toEntity(CategoriesRequest request);

    CategoriesResponse toResponse(Categories entity);
}
