package it.project.weather.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void appointCookie(String sessionUuid, HttpServletResponse response){

        Cookie cookie = new Cookie("personal_key", sessionUuid);
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    public void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("personal_key", "for_delete");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }





}
