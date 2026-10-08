package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "location_purchase")
public class LocationPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "invoice_amount")
    private BigDecimal invoiceAmount;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "payment")
    private String payment;

    @Column(name = "invoice")
    private String invoice;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "aging")
    private BigDecimal  aging;

    @Column(name = "possible_duplicate")
    private String possibleDuplicate;
}