package com.skillbridgebackend.bookingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateBookingRequest(
        @NotBlank String mentorId,
        @NotNull Instant slotTs,
        @NotNull @DecimalMin("0.0") BigDecimal price
) {}