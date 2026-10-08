package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseDto {

    private Long id;

    private Long locationId;

    private String locationName;

    private String organization;

    private LocalDate purchaseDate;

    private LocalDate invoiceDate;

    private BigDecimal invoiceAmount;

    private String vendor;

    private String payment;

    private String invoice;

    private String comments;

    private LocalDateTime createdOn;

    private BigDecimal aging;

    private String possibleDuplicate;
}