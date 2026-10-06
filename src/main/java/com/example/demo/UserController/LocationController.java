package com.example.demo.UserController;

import com.example.demo.entity.Location;
import com.example.demo.repository.LocationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class LocationController {

    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping("/locations")
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
}