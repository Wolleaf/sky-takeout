package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sky.shop")
public class ShopProperties {
    // 商家地址
    private String address;
    // 服务距离
    private Integer serviceDistance;
    // 百度地图的ak
    private String ak;
}
