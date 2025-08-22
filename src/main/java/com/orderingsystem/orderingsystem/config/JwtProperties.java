package com.orderingsystem.orderingsystem.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtProperties {
    private String accessSecret;
    private String refreshSecret;
    private long accessTtl;
    private long refreshTtl;
}