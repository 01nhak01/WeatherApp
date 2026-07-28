package com.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class WeatherappApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WeatherController weatherController;

	@Autowired
	private Weather_Aqi weather_Aqi;

	@Autowired
	private Weather_Dewpoint weather_Dewpoint;

	@Autowired
	private Weather_Humidity weather_Humidity;

	@Autowired
	private Weather_Map weather_Map;

	@Autowired
	private Weather_Pressure weather_Pressure;

	@Autowired
	private Weather_Sunrise_sunset weather_Sunrise_sunset;

	@Autowired
	private Weather_Uv weather_Uv;

	@Autowired
	private Weather_Visibility weather_Visibility;

	@Autowired
	private Weather_Wind weather_Wind;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(
				weatherController,
				weather_Aqi,
				weather_Dewpoint,
				weather_Humidity,
				weather_Map,
				weather_Pressure,
				weather_Sunrise_sunset,
				weather_Uv,
				weather_Visibility,
				weather_Wind
		).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testIndexPage() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	void testWeatherPage() throws Exception {
		mockMvc.perform(get("/weather").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testWeatherApi() throws Exception {
		mockMvc.perform(get("/api/weather").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testAqiPage() throws Exception {
		mockMvc.perform(get("/weather/aqi").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testDewpointPage() throws Exception {
		mockMvc.perform(get("/weather/dewpoint").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testHumidityPage() throws Exception {
		mockMvc.perform(get("/weather/humidity").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testMapPage() throws Exception {
		mockMvc.perform(get("/weather/map").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testPressurePage() throws Exception {
		mockMvc.perform(get("/weather/pressure").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testSunriseSunsetPage() throws Exception {
		mockMvc.perform(get("/weather/sunrise_sunset").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testUvPage() throws Exception {
		mockMvc.perform(get("/weather/uv").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testVisibilityPage() throws Exception {
		mockMvc.perform(get("/weather/visibility").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testWindPage() throws Exception {
		mockMvc.perform(get("/weather/wind").param("city", "Hanoi"))
				.andExpect(status().isOk());
	}

	@Test
	void testSearchApi() throws Exception {
		mockMvc.perform(get("/api/weather/search").param("query", "Hanoi"))
				.andExpect(status().isOk());
	}
}
