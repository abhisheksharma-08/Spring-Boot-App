package com.expdiary.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expdiary.journalApp.entity.User;
import com.expdiary.journalApp.services.UserService;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user) {
        userService.saveNewEntry(user);
    }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // public ResponseEntity<String> handleAllExceptions(Exception ex) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("An error
    // occurred: " + ex.getMessage());
    // }
}
