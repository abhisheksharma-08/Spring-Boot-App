package com.expdiary.journalApp.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expdiary.journalApp.api.resource.WeatherResponse;
import com.expdiary.journalApp.entity.User;
import com.expdiary.journalApp.repository.UserRepository;
import com.expdiary.journalApp.services.UserService;
import com.expdiary.journalApp.services.WeatherService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    WeatherService weatherService;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAll();
    }

    // @PostMapping
    // public void createUser(@RequestBody User user) {
    // userService.saveNewEntry(user);
    // }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);

        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewEntry(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@RequestBody User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/hey")
    public ResponseEntity<?> greet() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WeatherResponse weatherResponse = weatherService.getWeather("Meerut");
        if (weatherResponse != null) {
            return new ResponseEntity<>("hello" + authentication.getName() + "Weather Feels Like :"
                    + weatherResponse.getCurrent().getFeelslike(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);

    }

}
