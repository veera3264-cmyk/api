package com.example.demo.UserController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public Map<String, String> home() {
        return Map.of("message", "API is working");
    }

    @GetMapping("/health")
    public String health() {
        return "Application is running";
    }

    @GetMapping("/login")
    public String loginGet() {
        return "API working fine";
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            response.put("status", "FAILED");
            response.put("message", "Account already exists with this email");
            return response;
        }

        userRepository.save(user);

        response.put("status", "SUCCESS");
        response.put("message", "Account Created Successfully");

        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User request) {

        Map<String, String> response = new HashMap<>();

        User user = userRepository.findByEmail(request.getEmail());

        if (user != null &&
                user.getPassword().equals(request.getPassword())) {

            response.put("status", "SUCCESS");
            response.put("message", "Login Successful");
            response.put("username", user.getUsername());
        } else {

            response.put("status", "FAILED");
            response.put("message", "Invalid Credentials");
        }

        return response;
    }
}