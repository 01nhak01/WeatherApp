package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class Weather_Aqi {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/aqi")
    public String getAqiDetails(@RequestParam(defaultValue = "Hanoi") String city, Model model) {
        try {
            Map<String, Object> data = weatherService.getWeatherData(city);
            model.addAttribute("data", data);
            model.addAttribute("city", city);
            model.addAttribute("apiKey", apiKey);
        } catch (Exception e) {
            model.addAttribute("error", "Không tìm thấy thành phố!");
        }
        return "Weather_Aqi";
    }
}
