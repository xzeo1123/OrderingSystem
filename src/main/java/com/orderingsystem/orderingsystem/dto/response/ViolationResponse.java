package com.orderingsystem.orderingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViolationResponse {
    private String field;
    private Object rejectedValue;
    private String reason;
}
