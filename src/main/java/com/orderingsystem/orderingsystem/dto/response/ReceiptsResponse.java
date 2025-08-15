package com.orderingsystem.orderingsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.orderingsystem.orderingsystem.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptsResponse {

    private Integer id;

    private BigDecimal total;

    private LocalDateTime dateCreate;

    private String note;

    private Status status;
}
