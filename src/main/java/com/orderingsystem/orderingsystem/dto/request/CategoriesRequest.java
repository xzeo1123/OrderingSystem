package com.orderingsystem.orderingsystem.dto.request;

import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriesRequest {

    @NotBlank(message = "Category name cannot be empty")
    private String name;

    @NotBlank(message = "Category description cannot be empty")
    private String description;

    private Status status;
}
