package it.project.weather.repository;

import it.project.weather.entity.Session;
import it.project.weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    Optional<Session> findById(String id);

    Optional<Session> findByUser(User userId);



}
