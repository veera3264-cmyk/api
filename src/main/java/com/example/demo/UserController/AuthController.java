package com.example.demo.UserController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !Objects.equals(user.getPassword(), request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // Pass user role (defaults to "ROLE_USER" if entity doesn't provide one)
        String userRole = (user.getRole() != null) ? user.getRole() : "ROLE_USER";
        String token = jwtService.generateToken(user.getEmail(), "ROLE_USER");

        return ResponseEntity.ok(
                Map.of(
                        "status", "SUCCESS",
                        "message", "Login successful",
                        "email", user.getEmail(),
                        "token", token
                )
        );

    }
}