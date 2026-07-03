package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class WeatherController {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("apiKey", apiKey);
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        try {
            Map<String, Object> data = weatherService.getWeatherData(city);
            model.addAttribute("data", data);
        } catch (Exception e) {
            model.addAttribute("error", "Không tìm thấy thành phố!");
        }
        return "index";
    }

    @GetMapping("/api/weather")
    @ResponseBody
    public ResponseEntity<?> getWeatherApi(@RequestParam(defaultValue = "Hanoi") String city) {
        try {
            Map<String, Object> data = weatherService.getWeatherData(city);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}