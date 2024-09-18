package it.project.weather.service;

import it.project.weather.entity.User;
import it.project.weather.model.UserDto;
import it.project.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {



    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public void save(UserDto userDto) {
        User newUser = modelMapper.map(userDto, User.class);
        userRepository.save(newUser);
    }




}
