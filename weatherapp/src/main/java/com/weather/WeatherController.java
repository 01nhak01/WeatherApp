package com.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Controller
public class WeatherController 
{

    @Value("${weather.api.key}") // Lấy giá trị từ application.properties
    private String apiKey;

    @GetMapping("/")
    public String index() {
        return "index"; // Trả về giao diện index.html
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";
        
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Lấy dữ liệu dạng JSON từ API
            Object data = restTemplate.getForObject(url, Object.class);
            model.addAttribute("data", data);
        } catch (Exception e) {
            model.addAttribute("error", "Không tìm thấy thành phố!");
        }
        return "index";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/api/weather")
    @ResponseBody
    public ResponseEntity<?> getWeatherApi(@RequestParam(defaultValue = "Hanoi") String city) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Fetch current weather
            String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=vi";
            Map<String, Object> currentData = restTemplate.getForObject(currentWeatherUrl, Map.class);
            if (currentData == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy thành phố!"));
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
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không tìm thấy thành phố hoặc lỗi hệ thống: " + e.getMessage()));
        }
    }
}