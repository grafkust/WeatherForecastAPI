package it.project.weather.api;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class RequestToApiTest {

    @Value("${api.key}")
    private String key;

    private final String apiURL = "https://api.weatherapi.com/";

    private final String getForecastByCoordinateURL = "v1/current.json?aqi=no&lang=ru&key=";
    private final String getLocationsByNameURL ="v1/search.json?key=";

    //test data
    private final Float testCityLat = 48.87f;
    private final Float testCityLon = 2.33f;
    private final String testCityName = "Paris";
    private final String testCityRegion = "Ile-de-France";


    @Test
    public void getLocationByNameTest() {

        Specification.installSpecification(Specification.requestSpec(apiURL), Specification.responseSpecUniqueCode(200));

        String message = "api error";

        Response response = given()
                .when()
                .get(getLocationsByNameURL + key + "&q=" + testCityName)
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<String> locationsName = jsonPath.get("name");
        List<String> locationsRegions = jsonPath.get("region");
        List<Float> locationsLat = jsonPath.get("lat");
        List<Float> locationsLon = jsonPath.get("lon");

        Assert.isTrue(locationsName.contains(testCityName),message);
        Assert.isTrue(locationsRegions.contains(testCityRegion), message);
        Assert.isTrue(locationsLat.stream().anyMatch(x -> x.equals(testCityLat)),message);
        Assert.isTrue(locationsLon.contains(testCityLon),message);
    }

    @Test
    public void getForecastByCoordinatesTest() {
        Specification.installSpecification(Specification.requestSpec(apiURL), Specification.responseSpecUniqueCode(200));

        String message = "api error";

        String coordinates = testCityLat + "," + testCityLon;

        Response response = given()
                .when()
                .get(getForecastByCoordinateURL + key + "&q=" +  coordinates)
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String locationName = jsonPath.get("location.name");
        String locationRegion = jsonPath.get("location.region");

        Assert.isTrue(locationName.equals(testCityName), message);
        Assert.isTrue(locationRegion.equals(testCityRegion), message);
    }

    @Test
    public void coordinatesIsMissingExceptionTest() {
        Specification.installSpecification(Specification.requestSpec(apiURL), Specification.responseSpecUniqueCode(400));

        given()
                .when()
                .get(getForecastByCoordinateURL + key + "&q=")
                .then().log().all()
                .extract().response();
    }

    @Test
    public void nameIsMissingExceptionTest() {
        Specification.installSpecification(Specification.requestSpec(apiURL), Specification.responseSpecUniqueCode(400));

        given()
                .when()
                .get(getLocationsByNameURL + key + "&q=")
                .then().log().all()
                .extract().response();
    }
}
