package com.orderingsystem.orderingsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloudinary.default.product.image")
@Data
public class CloudinaryProperties {
    private String id;
    private String url;
}
