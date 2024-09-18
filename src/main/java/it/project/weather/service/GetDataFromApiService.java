package it.project.weather.service;

import it.project.weather.model.ForecastDto;
import it.project.weather.model.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDataFromApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.key}")
    private String key;

    public ForecastDto getForecastByCoordinates(double latitude, double longitude) {

        String currentForecastUrl = "https://api.weatherapi.com/v1/current.json?key=" + key + "&aqi=no&lang=ru&q=";
        String request = currentForecastUrl + latitude + "," + longitude;

        return restTemplate.getForEntity(request, ForecastDto.class).getBody();
    }

    public List searchLocationByName (String word) {

        String searchUrl = "https://api.weatherapi.com/v1/search.json?key=" + key + "&q=";
        String request  = searchUrl + word;

        return restTemplate.getForObject(request, List.class);
    }



}
