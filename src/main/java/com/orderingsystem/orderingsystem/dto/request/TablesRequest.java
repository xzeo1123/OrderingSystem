package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TablesRequest {

    @NotNull(message = "Table number cannot be empty")
    private Integer number;

    private Byte status;
}
