package com.example.demo.UserController;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;



@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
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
        System.out.println("REQUEST PASSWORD = " + request.getPassword());

        if (user != null &&
                user.getPassword().equals(request.getPassword())) {

            String token =
                    jwtService.generateToken(
                            user.getEmail(),
                            "USER"
                    );

            return Map.of(
                    "status", "SUCCESS",
                    "message", "Login successful",
                    "email", user.getEmail(),
                    "token", token
            );
        }

        return Map.of(
                "status", "FAILED",
                "message", "Invalid credentials"
        );
    }
}