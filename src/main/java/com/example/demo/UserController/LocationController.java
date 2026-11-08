package com.example.demo.UserController;

import com.example.demo.entity.Location;
import com.example.demo.repository.LocationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
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