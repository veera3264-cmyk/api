package com.example.demo.service;

import com.example.demo.dto.PurchaseDto;
import com.example.demo.repository.LocationPurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    private final LocationPurchaseRepository repository;

    public PurchaseService(LocationPurchaseRepository repository) {
        this.repository = repository;
    }

    public List<PurchaseDto> getPurchases() {

        List<Object[]> rows = repository.getPurchaseData();

        List<PurchaseDto> result = new ArrayList<>();

        for (Object[] row : rows) {

            PurchaseDto dto = new PurchaseDto();

            dto.setId(
                    row[0] != null
                            ? ((Number) row[0]).longValue()
                            : null);

            dto.setLocationId(
                    row[1] != null
                            ? ((Number) row[1]).longValue()
                            : null);

            dto.setLocationName(
                    row[2] != null
                            ? row[2].toString()
                            : null);

            dto.setOrganization(
                    row[3] != null
                            ? row[3].toString()
                            : null);

            dto.setPurchaseDate(
                    row[4] != null
                            ? LocalDate.parse(row[4].toString())
                            : null);

            dto.setInvoiceDate(
                    row[5] != null
                            ? LocalDate.parse(row[5].toString())
                            : null);

            dto.setInvoiceAmount(
                    row[6] != null
                            ? new BigDecimal(row[6].toString())
                            : null);

            dto.setVendor(
                    row[7] != null
                            ? row[7].toString()
                            : null);

            dto.setPayment(
                    row[8] != null
                            ? row[8].toString()
                            : null);

            dto.setInvoice(
                    row[9] != null
                            ? row[9].toString()
                            : null);

            dto.setComments(
                    row[10] != null
                            ? row[10].toString()
                            : null);

            dto.setCreatedOn(
                    row[11] != null
                            ? LocalDateTime.parse(
                            row[11].toString().replace(" ", "T"))
                            : null);

            dto.setAging(
                    row[12] != null
                            ? new BigDecimal(row[12].toString())
                            : null);

            dto.setPossibleDuplicate(
                    row[13] != null
                            ? row[13].toString()
                            : null);

            result.add(dto);
        }

        return result;
    }
}