package it.project.weather.service;

import it.project.weather.entity.Session;
import it.project.weather.entity.User;
import it.project.weather.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    @Value("${sessionLiveSpanInDays}")
    private int sessionLifeSpan;


    public Optional<Session> findById(String id) {
        return sessionRepository.findById(id);
    }

    public Optional<Session> findByUser(User user) {
        return sessionRepository.findByUser(user);
    }

    public void save(Session session) {
        sessionRepository.save(session);
    }




    public String appointSession(User user) {

        Optional<Session> userSession = sessionRepository.findByUser(user);
        userSession.ifPresent(sessionRepository::delete);

        Calendar expiresAt = new GregorianCalendar();
        expiresAt.add(Calendar.DAY_OF_WEEK, sessionLifeSpan);

        Session newSession = new Session(user, expiresAt);

        sessionRepository.save(newSession);
        return newSession.getId();
    }

    public boolean sessionIsExistAndContinue(String personalKey) {
        Calendar currentDate = new GregorianCalendar();
        Optional<Session> userSession = sessionRepository.findById(personalKey);
        return userSession.isPresent() && userSession.get().getExpiresAt().after(currentDate);
    }

    public boolean sessionIsExpired(Session userSession) {
        Calendar currentDate = new GregorianCalendar();
        return userSession.getExpiresAt().before(currentDate);
    }
}
