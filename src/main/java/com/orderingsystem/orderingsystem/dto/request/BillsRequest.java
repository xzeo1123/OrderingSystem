package com.orderingsystem.orderingsystem.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillsRequest {

    @NotNull(message = "Total bill cannot be empty")
    private Float total;

    private LocalDateTime dateCreate;

    private String note;

    private Byte status;

    @NotNull(message = "User id cannot be empty")
    private Integer userId;

    @NotNull(message = "Table id cannot be empty")
    private Integer tableId;
}
