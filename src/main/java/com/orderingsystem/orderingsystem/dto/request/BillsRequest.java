package com.orderingsystem.orderingsystem.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillsRequest {

    @NotNull(message = "Total cannot be empty")
    @Min(value = 0, message = "Total cannot be lower than 0")
    private BigDecimal total;

    private LocalDateTime dateCreate;

    @NotBlank(message = "Note cannot be empty")
    private String note;

    private Status status;

    @NotNull(message = "User ID cannot be empty")
    @Min(value = 1, message = "User ID cannot be lower than 1")
    private Integer userId;

    @NotNull(message = "Table ID cannot be empty")
    @Min(value = 1, message = "Talbe ID cannot be lower than 0")
    private Integer tableId;
}
