package com.orderingsystem.orderingsystem.dto.request;

import java.time.LocalDateTime;

import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReceiptsRequest {

    @NotNull(message = "Total bill cannot be empty")
    @Min(value = 0, message = "Total must be greater than or equal to 0")
    private Float total;

    private LocalDateTime dateCreate;

    @NotBlank(message = "Note cannot be empty")
    private String note;

    private Status status;
}
