package com.expdiary.journalApp.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expdiary.journalApp.entity.User;

import com.expdiary.journalApp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // create
    public void saveEntry(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception", e);
        }

    }

    public void saveNewEntry(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception", e);
        }

    }

    public void saveAdmin(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER", "ADMIN"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    // get
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // getbyid
    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    // delete
    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

}
