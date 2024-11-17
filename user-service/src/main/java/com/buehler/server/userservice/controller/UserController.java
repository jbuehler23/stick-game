package com.buehler.server.userservice.controller;

import com.buehler.server.userservice.entity.User;
import com.buehler.server.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/{id}/deduct")
    public void deductPoints(@PathVariable Long id, @RequestParam int points) {
        userService.deductPoints(id, points);
    }
}

