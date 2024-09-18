package it.project.weather.controller;

import it.project.weather.entity.Session;
import it.project.weather.entity.User;
import it.project.weather.model.ForecastDto;
import it.project.weather.model.LocationDto;
import it.project.weather.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;
    private final GetDataFromApiService apiService;
    private final LocationService locationService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @GetMapping("/user-page")
    public String userPage(@CookieValue(value = "personal_key", defaultValue = "") String personalKey,
                           @RequestParam (name = "search-city", required = false) String cityName,
                           @RequestParam (name = "error", required = false) String error,
                           Model model) {

        Optional <Session> userSession = sessionService.findById(personalKey);

        if (userSession.isEmpty())
            return "redirect:/";

        if (sessionService.sessionIsExpired(userSession.get()))
            return "redirect:/?session-status=time-up";

        if (error != null)
            model.addAttribute("error", "В вашем списке городов уже есть выбранная локация.");

        if (cityName != null && !cityName.isEmpty()) {

            List listOfLocations = apiService.searchLocationByName(cityName);

            if (listOfLocations.isEmpty())
                model.addAttribute("error", "По данному запросу не найдено ни одного города.");
            else
                model.addAttribute("message", "Выберите город");

            model.addAttribute("listOfLocations", listOfLocations);
        }

        User user = userSession.get().getUser();
        List<ForecastDto> forecastsForUserLocations = locationService.getForecastsForUserLocations(user);
        LocationDto locationDto = new LocationDto();

        model.addAttribute("forecastsForUserLocations", forecastsForUserLocations);
        model.addAttribute("user", user);
        model.addAttribute("locationDto", locationDto);

        return "user-page";
    }


    @PostMapping("/add-location")
    public String addLocation(LocationDto locationDto ){

        if (locationService.checkLocationIsExist(locationDto))
            return "redirect:/user-page?error=true";

        User user = userService.findById(locationDto.getUserId());

        locationService.save(locationDto, user);

        return "redirect:/user-page";
    }


    @PostMapping("/delete-location")
    public String deleteLocation(LocationDto locationDto) {

        locationService.delete(locationDto);

        return "redirect:/user-page";

    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        cookieService.deleteCookie(response);
        return "redirect:/";
    }




}
