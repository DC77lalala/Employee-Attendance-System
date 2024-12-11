package com.wdc.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class BaiduMapService {

    @Value("${baidu.map.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public BaiduMapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLocationName(double latitude, double longitude) {
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey + 
                     "&output=json&coordtype=wgs84ll&location=" + latitude + "," + longitude;

        // 发送GET请求，获取API返回的地理信息
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 解析返回的JSON，提取地理位置名称
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            // 在这里解析返回的JSON，获取打卡地点
            return responseBody;  // 返回的是原始的JSON，您可以根据需求提取具体字段
        }

        return "获取地理位置失败";
    }
}
