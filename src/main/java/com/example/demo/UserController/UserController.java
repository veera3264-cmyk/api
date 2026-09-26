package com.example.demo.UserController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return Map.of(
                    "status", "FAILED",
                    "message", "Account already exists with this email"
            );
        }

        userRepository.save(user);

        return Map.of(
                "status", "SUCCESS",
                "message", "Account created successfully"
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user != null &&
                user.getPassword().equals(request.getPassword())) {

            return Map.of(
                    "status", "SUCCESS",
                    "message", "Login successful",
                    "email", user.getEmail()
            );
        }

        return Map.of(
                "status", "FAILED",
                "message", "Invalid credentials"
        );
    }
}