package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWeatherData(String city) {
        Map<String, Object> response = new HashMap<>();

        // 1. Fetch current weather
        String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";
        Map<String, Object> currentData = restTemplate.getForObject(currentWeatherUrl, Map.class);
        if (currentData == null) {
            throw new RuntimeException("Không tìm thấy thành phố!");
        }
        response.put("current", currentData);

        // Extract lat and lon to fetch air pollution
        Map<String, Object> coord = (Map<String, Object>) currentData.get("coord");
        if (coord != null) {
            double lat = ((Number) coord.get("lat")).doubleValue();
            double lon = ((Number) coord.get("lon")).doubleValue();

            // 2. Fetch air quality
            try {
                String airPollutionUrl = "https://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
                Map<String, Object> airData = restTemplate.getForObject(airPollutionUrl, Map.class);
                response.put("airQuality", airData);
            } catch (Exception e) {
                System.err.println("Error fetching air quality: " + e.getMessage());
            }
        }

        // 3. Fetch forecast
        try {
            String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";
            Map<String, Object> forecastData = restTemplate.getForObject(forecastUrl, Map.class);
            response.put("forecast", forecastData);
        } catch (Exception e) {
            System.err.println("Error fetching forecast: " + e.getMessage());
        }

        return response;
    }
}
