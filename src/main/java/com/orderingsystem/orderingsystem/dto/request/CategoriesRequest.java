package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriesRequest {

    @NotBlank(message = "Category name cannot be empty")
    private String name;

    @NotBlank(message = "Category description cannot be empty")
    private String description;

    private Byte status;
}
