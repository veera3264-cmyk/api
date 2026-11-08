package com.example.demo.UserController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request,
                                                     HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !Objects.equals(
                user.getPassword(),
                request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String userRole = (user.getRole() != null && !user.getRole().isBlank())
                ? user.getRole()
                : "ROLE_USER";
        String token = jwtService.generateToken(
                user.getEmail(),
                userRole
        );

        ResponseCookie cookie =
                ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // true in AWS/HTTPS
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
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return ResponseEntity.ok(
                Map.of(
                        "email",
                        authentication.getName()
                )
        );
    }
}