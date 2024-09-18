package it.project.weather.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class LocationDto {

    private String name;

    private Integer userId;

    private double latitude;

    private double longitude;




}
