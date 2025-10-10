package com.orderingsystem.orderingsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLoginRequest {

    @NotBlank(message = "Provider cannot be empty")
    private String provider;

    @NotBlank(message = "Access token cannot be empty")
    private String accessToken;
}
