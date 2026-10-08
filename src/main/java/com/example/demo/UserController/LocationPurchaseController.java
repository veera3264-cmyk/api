package com.example.demo.UserController;

import com.example.demo.dto.PurchaseDto;
import com.example.demo.entity.LocationPurchase;
import com.example.demo.repository.LocationPurchaseRepository;
import com.example.demo.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class LocationPurchaseController {

    private final PurchaseService purchaseService;
    private final LocationPurchaseRepository locationPurchaseRepository;

    public LocationPurchaseController(
            PurchaseService purchaseService,
            LocationPurchaseRepository locationPurchaseRepository) {

        this.purchaseService = purchaseService;
        this.locationPurchaseRepository = locationPurchaseRepository;
    }

    @GetMapping("/purchases")
    public List<PurchaseDto> getPurchases() {
        return purchaseService.getPurchases();
    }

    @PostMapping("/purchases")
    public LocationPurchase addPurchase(
            @RequestBody LocationPurchase purchase) {

        return locationPurchaseRepository.save(purchase);
    }
}