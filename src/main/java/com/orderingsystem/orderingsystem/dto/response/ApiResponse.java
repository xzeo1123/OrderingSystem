package com.orderingsystem.orderingsystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String message;
    private T data;
}
