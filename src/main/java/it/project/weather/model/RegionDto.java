package it.project.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RegionDto {

    @Setter
    private String name;

    private String country;

    private double lat;

    private double lon;

}
