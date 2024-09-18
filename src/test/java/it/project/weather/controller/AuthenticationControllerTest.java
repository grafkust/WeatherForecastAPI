package it.project.weather.controller;

import it.project.weather.entity.User;
import it.project.weather.model.UserDto;
import it.project.weather.service.SessionService;
import it.project.weather.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Optional;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
public class AuthenticationControllerTest {

    private final String login = "testLogin";
    private final String password = "testPassword";
    private final UserDto userDto = new UserDto(login, password);

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Test
    @Sql(value = {"classpath:clean-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void regNewUserTest() {

        authenticationController.regUser(userDto);

        Optional<User> user = userService.findByLoginAndPassword(login, password);

        Assert.isTrue(user.isPresent(), "");

    }

    @Test
    @Sql(value = {"classpath:clean-tables-and-add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void tryToRegUserWithNonUniqueNameTest() {

        String request = authenticationController.regUser(userDto);

        Assert.isTrue(request.equals("redirect:/reg?error=log-error"), "");

    }

    @Test
    @Sql(value = {"classpath:clean-tables-and-add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void logExistUserTest() {

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        String response = authenticationController.logUser(userDto, mockResponse);

        Assert.isTrue(response.equals("redirect:/user-page"), "");
    }

    @Test
    @Sql(value = {"classpath:clean-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void logUnknownUserTest() {

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        String response = authenticationController.logUser(userDto, mockResponse);

        Assert.isTrue(response.equals("redirect:/?error=log-error"), "");
    }

    @Test
    @Sql(value = {"classpath:clean-tables-and-add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void appointSessionAfterLogTest() {

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        User user = userService.findByLoginAndPassword(userDto.getLogin(), userDto.getPassword()).get();

        Assert.isTrue(sessionService.findByUser(user).isEmpty(), "");

        authenticationController.logUser(userDto, mockResponse);

        Assert.isTrue(sessionService.findByUser(user).isPresent() , "");
    }
}
