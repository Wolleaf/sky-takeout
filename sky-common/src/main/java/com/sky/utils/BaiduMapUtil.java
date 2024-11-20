package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.properties.ShopProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaiduMapUtil {

    private final ShopProperties shopProperties;

    /**
     * 判断地址是否在配送范围内
     *
     * @param address
     * @return
     */
    public boolean isInServiceArea(String address) {
        String origin = getLatLng(shopProperties.getAddress());
        String destination = getLatLng(address);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("origin", origin);
        paramMap.put("destination", destination);
        paramMap.put("ak", shopProperties.getAk());
        String url = "https://api.map.baidu.com/directionlite/v1/driving";
        String json = HttpClientUtil.doGet(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer distance = jsonObject.getJSONObject("result")
                .getJSONArray("routes")
                .getJSONObject(0)
                .getInteger("distance");
        return distance <= shopProperties.getServiceDistance();
    }

    /**
     * 获取经纬度
     * @param address
     * @return
     */
    private String getLatLng(String address) {
        // 1. 请求地址
        String url = "https://api.map.baidu.com/geocoding/v3/";
        // 2. 请求参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", address);
        paramMap.put("output", "json");
        paramMap.put("ak", shopProperties.getAk());
        // 2. 发送请求
        String json = HttpClientUtil.doGet(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        return location.getString("lat") + "," + location.getString("lng");
    }

}
