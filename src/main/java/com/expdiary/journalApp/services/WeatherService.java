package com.expdiary.journalApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.expdiary.journalApp.api.resource.WeatherResponse;

@Component
public class WeatherService {
    @Value("${api_key}")
    private String apiKey = "";
    private String API = "";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String finalApi = API.replace("CITY", city).replace("API_KEY", apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null,
                WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }

}
