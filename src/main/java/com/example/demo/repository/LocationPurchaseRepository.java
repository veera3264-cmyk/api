package com.example.demo.repository;

import com.example.demo.entity.LocationPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationPurchaseRepository
        extends JpaRepository<LocationPurchase, Long> {

    @Query(value = """
            SELECT
                lp.id,
                lp.location_id,
                l.location_name,
                l.organization,
                lp.purchase_date,
                lp.invoice_date,
                lp.invoice_amount,
                lp.vendor,
                lp.payment,
                lp.invoice,
                lp.comments,
                lp.created_on,
                lp.aging,
                lp.possible_duplicate
            FROM location_purchase lp
            LEFT JOIN location l
                ON lp.location_id = l.location_id
            """,
            nativeQuery = true)
    List<Object[]> getPurchaseData();
}