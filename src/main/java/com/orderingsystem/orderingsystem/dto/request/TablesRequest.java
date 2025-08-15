package com.orderingsystem.orderingsystem.dto.request;

import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TablesRequest {

    @NotNull(message = "Table number cannot be empty")
    @Min(value = 1, message = "Table number must be at least 1")
    private Integer number;

    private Status status;
}
