package com.example.demo.UserController;

import com.example.demo.entity.LocationPurchase;
import com.example.demo.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/assignments", "/auth/assignments"})
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/auto-assign")
    public List<LocationPurchase> autoAssignPurchases() {
        return assignmentService.autoAssignPurchases();
    }

    @GetMapping("/my-purchases")
    public List<LocationPurchase> getMyAssignments() {
        return assignmentService.getMyAssignments();
    }
}
