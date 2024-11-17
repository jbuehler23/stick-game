package com.buehler.server.userservice.service;

import com.buehler.server.userservice.entity.User;
import com.buehler.server.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deductPoints(Long userId, int points) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPoints(user.getPoints() - points);
        userRepository.save(user);
    }
}

