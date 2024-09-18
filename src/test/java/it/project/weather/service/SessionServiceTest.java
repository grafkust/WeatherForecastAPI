package it.project.weather.service;

import it.project.weather.entity.Session;
import it.project.weather.entity.User;
import it.project.weather.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    private final String login = "testLogin";
    private final String password = "testPassword";
    private final UserDto userDto = new UserDto(login, password);

    @Test
    @Sql(value = {"classpath:clean-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"classpath:clean-tables-and-add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void checkSessionExpires() {

        User user = userService.findByLoginAndPassword(userDto.getLogin(), userDto.getPassword()).get();

        String sessionUuid = sessionService.appointSession(user);
        Session userSession = sessionService.findById(sessionUuid).get();

        Assert.isTrue(!sessionService.sessionIsExpired(userSession), "");
        Assert.isTrue(sessionService.sessionIsExistAndContinue(userSession.getId()), "");

        Calendar expiredSessionTime = new GregorianCalendar();
        expiredSessionTime.add(Calendar.MINUTE, -1);

        userSession.setExpiresAt(expiredSessionTime);

        sessionService.save(userSession);

        Assert.isTrue(sessionService.sessionIsExpired(userSession), "");
        Assert.isTrue(!sessionService.sessionIsExistAndContinue(userSession.getId()), "");
    }
}
