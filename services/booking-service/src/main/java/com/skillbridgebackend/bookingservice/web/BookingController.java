package com.skillbridgebackend.bookingservice.web;

import com.skillbridgebackend.bookingservice.domain.Booking;
import com.skillbridgebackend.bookingservice.dto.CreateBookingRequest;
import com.skillbridgebackend.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService svc;
    public BookingController(BookingService svc){ this.svc = svc; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@AuthenticationPrincipal Jwt jwt,
                          @RequestBody @Valid CreateBookingRequest req){
        String menteeId = jwt != null ? jwt.getSubject() : "anonymous";
        return svc.create(menteeId, req);
    }

    @GetMapping("/{id}")
    public Booking get(@PathVariable Long id){ return svc.get(id); }
}
