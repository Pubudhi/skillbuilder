package com.skillbridgebackend.bookingservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity @Table(name = "bookings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"mentorId","slotTs"}))
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mentorId;
    private String menteeId;
    private Instant slotTs;
    private String status; // REQUESTED, CONFIRMED, CANCELLED
    private BigDecimal price;
    private String paymentIntentId;

}