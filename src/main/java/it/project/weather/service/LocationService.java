package it.project.weather.service;

import it.project.weather.entity.Location;
import it.project.weather.entity.User;
import it.project.weather.model.ForecastDto;
import it.project.weather.model.LocationDto;
import it.project.weather.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final GetDataFromApiService apiService;

    private final ModelMapper modelMapper;


    public void save(LocationDto locationDto, User user) {

        Location location = modelMapper.map(locationDto, Location.class);
        location.setUserId(user);
        locationRepository.save(location);
    }


    @Transactional
    public void delete(LocationDto locationDto) {

        locationRepository.deleteByUserId_IdAndLatitudeAndLongitude(locationDto.getUserId(),
                locationDto.getLatitude(),
                locationDto.getLongitude());
    }

    public boolean checkLocationIsExist(LocationDto locationDto) {
        return locationRepository.existsByUserId_IdAndLatitudeAndLongitude(locationDto.getUserId(),
                locationDto.getLatitude(),
                locationDto.getLongitude());
    }

    public List<ForecastDto> getForecastsForUserLocations(User user) {

        List<Location> userLocations = locationRepository.findAllByUserId(user);
        List<ForecastDto> forecastsForLocations = new ArrayList<>();

        for (Location location : userLocations) {

            ForecastDto singleForecast = apiService.getForecastByCoordinates(location.getLatitude(), location.getLongitude());
            singleForecast.getLocation().setName(location.getName());
            forecastsForLocations.add(singleForecast);
        }
        return forecastsForLocations;

    }



}
