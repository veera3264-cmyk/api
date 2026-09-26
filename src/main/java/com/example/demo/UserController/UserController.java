package com.example.demo.UserController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

<<<<<<< HEAD
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
=======
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
>>>>>>> b64fa8e5b9df9780bb6e727fab38330ddd1f851f
        }

        userRepository.save(user);

<<<<<<< HEAD
        response.put("status", "SUCCESS");
        response.put("message", "Account Created Successfully");

        return response;
=======
        return Map.of(
                "status", "SUCCESS",
                "message", "Account created successfully"
        );
>>>>>>> b64fa8e5b9df9780bb6e727fab38330ddd1f851f
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User request) {

<<<<<<< HEAD
        Map<String, String> response = new HashMap<>();

=======
>>>>>>> b64fa8e5b9df9780bb6e727fab38330ddd1f851f
        User user = userRepository.findByEmail(request.getEmail());

        if (user != null &&
                user.getPassword().equals(request.getPassword())) {

<<<<<<< HEAD
            response.put("status", "SUCCESS");
            response.put("message", "Login Successful");
            response.put("username", user.getUsername());
        } else {

            response.put("status", "FAILED");
            response.put("message", "Invalid Credentials");
        }

        return response;
=======
            return Map.of(
                    "status", "SUCCESS",
                    "message", "Login successful"
            );
        }

        return Map.of(
                "status", "FAILED",
                "message", "Invalid credentials"
        );
>>>>>>> b64fa8e5b9df9780bb6e727fab38330ddd1f851f
    }
}