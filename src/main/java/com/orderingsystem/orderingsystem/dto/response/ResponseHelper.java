package com.orderingsystem.orderingsystem.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseHelper {

    /* ---------- 200 OK ---------- */

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return build(HttpStatus.OK, data, message);
    }

    /* ---------- 201 Created ---------- */

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return build(HttpStatus.CREATED, data, message);
    }

    /* ---------- 204 No Content (logic “đã xoá”) ---------- */

    public static ResponseEntity<ApiResponse<Void>> deleted(String message) {
        return build(HttpStatus.OK, null, message);
    }

    /* ---------- Page Support ---------- */

    public static <T> ResponseEntity<ApiResponse<Page<T>>> paged(Page<T> page, String message) {
        return build(HttpStatus.OK, page, message);
    }

    /* ---------- Core builder ---------- */

    private static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, T data, String message) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .status(status.value())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
