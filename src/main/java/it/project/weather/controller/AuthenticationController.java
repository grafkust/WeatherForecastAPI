package it.project.weather.controller;

import it.project.weather.entity.User;
import it.project.weather.model.UserDto;
import it.project.weather.service.CookieService;
import it.project.weather.service.SessionService;
import it.project.weather.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @GetMapping("/")
    public String mainPage(@RequestParam(name = "error", defaultValue = "false") String error, Model model,
                           @RequestParam(name = "session-status", required = false) String sessionStatus,
                           @CookieValue(value = "personal_key", defaultValue = "") String personalKey){

        if (sessionService.sessionIsExistAndContinue(personalKey))
            return "forward:/user-page";

        if (error.equals("log-error"))
            model.addAttribute("errorMessage", "Вы допустили ошибку при вводе логина или пароля");

        if (sessionStatus != null)
            model.addAttribute("sessionStatus", "Время вашей предыдущей сессии истекло");

        return "main";
    }
    
    @PostMapping("/login")
    public String logUser (UserDto userDto, HttpServletResponse response) {

        Optional<User> user = userService.findByLoginAndPassword(userDto.getLogin(), userDto.getPassword());

        if (user.isEmpty())
           return "redirect:/?error=log-error";

        String newSessionUuid = sessionService.appointSession(user.get());

        cookieService.appointCookie(newSessionUuid, response);

        return "redirect:/user-page";
    }


    @GetMapping("/reg")
    public String regUser(@RequestParam(name = "error", defaultValue = "false") String error, Model model,
                          @CookieValue(value = "personal_key", defaultValue = "") String personalKey){

        if (sessionService.sessionIsExistAndContinue(personalKey))
            return "forward:/user-page";

        if (error.equals("log-error"))
            model.addAttribute("errorMessageLog", "Пользователь с таким ником уже существует");
        else if (error.equals("log-pass-error"))
            model.addAttribute("errorMessagePass", "Ненадежный пароль. Введите новый пароль");

        return "reg";
    }

    @PostMapping("/reg")
    public String regUser (UserDto userDto) {

        if (userService.existsByLogin(userDto.getLogin()))
            return "redirect:/reg?error=log-error";

        if (userDto.getLogin().length() < 3 || userDto.getPassword().length() < 6)
            return "redirect:/reg?error=log-pass-error";

        userService.save(userDto);

        return "redirect:/";
    }





}
