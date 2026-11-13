
package com.example.demo.UserController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // ---------------- SIGNUP ----------------

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(
            @RequestBody User user) {

        // Check whether email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        // Check whether username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username already exists"
            );
        }

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Assign default role
        user.setRole("ROLE_USER");

        // Save user in database
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "status", "SUCCESS",
                        "message", "Registration successful"
                ));
    }


    // ---------------- LOGIN ----------------

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        // Find user using email
        User user = userRepository.findByEmail(
                request.getEmail()
        );

        // Check user and password
        if (user == null ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        // Get user role
        String userRole =
                (user.getRole() != null &&
                        !user.getRole().isBlank())
                        ? user.getRole()
                        : "ROLE_USER";

        // Generate JWT token
        String token = jwtService.generateToken(
                user.getEmail(),
                userRole
        );

        // Create HTTP-only cookie
        ResponseCookie cookie =
                ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // Set true when using HTTPS
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ofHours(24))
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "SUCCESS",
                        "message", "Login successful",
                        "email", user.getEmail()
                )
        );
    }


    // ---------------- CURRENT USER ----------------

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "email",
                        authentication.getName()
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response) {
        ResponseCookie cookie =
                ResponseCookie.from("jwt", "")
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.noContent().build();
    }
}
