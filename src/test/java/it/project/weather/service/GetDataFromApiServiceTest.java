package it.project.weather.service;

import it.project.weather.model.ForecastDto;
import it.project.weather.model.LocationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class GetDataFromApiServiceTest {


    @Autowired
    private GetDataFromApiService getDataFromApiService;

    private final double testCityLat = 48.87;
    private final double testCityLon = 2.33;
    private final String testCityName = "Paris";
    private final String testCityCountry = "France";


    @Test
    public void getForecastByCoordinatesTest(){

        ForecastDto forecastByCoordinates = getDataFromApiService.getForecastByCoordinates(testCityLat, testCityLon);

        Assert.notNull(forecastByCoordinates.getLocation(), "");
        Assert.notNull(forecastByCoordinates.getCurrent(), "");
        Assert.notNull(forecastByCoordinates.getCurrent().getCondition(), "");

        Assert.isTrue(forecastByCoordinates.getLocation().getCountry().equals(testCityCountry), "");
        Assert.isTrue(forecastByCoordinates.getLocation().getName().equals(testCityName), "");
        Assert.isTrue(forecastByCoordinates.getLocation().getLat() == testCityLat, "");
        Assert.isTrue(forecastByCoordinates.getLocation().getLon() == testCityLon, "");
    }

    @Test
    public void searchLocationByNameTest(){
        List locationDto = getDataFromApiService.searchLocationByName(testCityName);

        Assert.notNull(locationDto, "");

    }

}
