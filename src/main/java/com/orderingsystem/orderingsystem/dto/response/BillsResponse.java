package com.orderingsystem.orderingsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orderingsystem.orderingsystem.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillsResponse {

    private Integer id;

    private BigDecimal total;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreate;

    private String note;

    private Status status;

    private Integer userId;

    private Integer tableId;
}
