package it.project.weather.repository;

import it.project.weather.entity.Location;
import it.project.weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    List<Location> findAllByUserId(User userId);

    boolean existsByUserId_IdAndLatitudeAndLongitude(Integer userId,  double lat, double lon);

    void deleteByUserId_IdAndLatitudeAndLongitude(Integer userId,  double lat, double lon);












}
