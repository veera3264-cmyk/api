package com.example.demo.service;

import com.example.demo.entity.LocationPurchase;
import com.example.demo.entity.User;
import com.example.demo.repository.LocationPurchaseRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private static final String DELIVERY_STATUS = "DELIVERIES";

    private final UserRepository userRepository;
    private final LocationPurchaseRepository locationPurchaseRepository;

    @Transactional
    public List<LocationPurchase> autoAssignPurchases() {
        User user = getAuthenticatedUser();

        int targetAssignments =
                getAssignmentCount(user.getSkillLevel());

        List<LocationPurchase> currentAssignments =
                locationPurchaseRepository
                        .findByAssignedToAndStatus(
                                user.getId(),
                                DELIVERY_STATUS);

        int currentCount = currentAssignments.size();
        int needed = targetAssignments - currentCount;

        if (needed <= 0) {
            return currentAssignments;
        }

        List<LocationPurchase> purchases =
                locationPurchaseRepository
                        .lockUnassignedDeliveryPurchases(
                                org.springframework.data.domain.PageRequest.of(0, needed));

        purchases.forEach(purchase -> {
            purchase.setAssignedTo(user.getId());
        });

        locationPurchaseRepository.saveAll(purchases);

        currentAssignments.addAll(purchases);

        return currentAssignments;
    }

    @Transactional(readOnly = true)
    public List<LocationPurchase> getMyAssignments() {
        User user = getAuthenticatedUser();
        return locationPurchaseRepository.findByAssignedToAndStatus(
                user.getId(),
                DELIVERY_STATUS);
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }
        return user;
    }

    private int getAssignmentCount(String skillLevel) {

        if (skillLevel == null || skillLevel.isBlank()) {
            return 5;
        }

        return switch (
                skillLevel.trim()
                        .toUpperCase(Locale.ROOT)) {

            case "NEW_JOINER" -> 5;
            case "BEGINNER" -> 10;
            case "INTERMEDIATE" -> 15;
            case "EXPERT" -> 20;
            default -> 5;
        };
    }
}