package com.orderingsystem.orderingsystem.mapping;

import com.orderingsystem.orderingsystem.dto.request.UsersRequest;
import com.orderingsystem.orderingsystem.dto.response.UsersResponse;
import com.orderingsystem.orderingsystem.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersResponse toResponse(Users entity);

    @Mapping(target = "id", ignore = true)
    Users toEntity(UsersRequest request);
}
