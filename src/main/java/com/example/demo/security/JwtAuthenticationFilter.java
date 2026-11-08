package com.example.demo.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.equals("/auth/login")
                || path.equals("/auth/signup");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || token.isBlank()) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            String email =
                    jwtService.extractEmail(token);

            if (email != null
                    && !email.isBlank()
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                if (jwtService.isTokenValid(token, email)) {

                    String role =
                            jwtService.extractRole(token);

                    List<SimpleGrantedAuthority> authorities =
                            (role != null && !role.isBlank())
                                    ? List.of(
                                    new SimpleGrantedAuthority(
                                            role.startsWith("ROLE_")
                                                    ? role
                                                    : "ROLE_" + role))
                                    : Collections.emptyList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities);

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {

            SecurityContextHolder.clearContext();

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write(
                    """
                    {
                      "error":"Unauthorized",
                      "message":"Invalid or expired JWT token"
                    }
                    """
            );

            return;
        }

        filterChain.doFilter(request, response);
    }
}