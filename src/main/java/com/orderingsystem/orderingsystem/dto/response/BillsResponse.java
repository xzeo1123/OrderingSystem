package com.orderingsystem.orderingsystem.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillsResponse {
    private Integer id;
    private Float total;
    private LocalDateTime dateCreate;
    private String note;
    private Byte status;
    private Integer userId;
    private Integer tableId;
}
