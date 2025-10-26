package com.skillbridgebackend.bookingservice.repo;

import com.skillbridgebackend.bookingservice.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
