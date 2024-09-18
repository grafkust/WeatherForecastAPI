package it.project.weather.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UserDto {

    private String login;

    private String password;
}
