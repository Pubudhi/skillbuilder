package com.skillbridgebackend.bookingservice.service;

import com.skillbridgebackend.bookingservice.domain.Booking;
import com.skillbridgebackend.bookingservice.dto.CreateBookingRequest;
import com.skillbridgebackend.bookingservice.repo.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private final BookingRepository repo;
    public BookingService(BookingRepository repo){ this.repo = repo; }

    @Transactional
    public Booking create(String menteeId, CreateBookingRequest req){
        Booking b = new Booking();
        b.setMenteeId(menteeId);
        b.setMentorId(req.mentorId());
        b.setSlotTs(req.slotTs());
        b.setPrice(req.price());
        b.setStatus("REQUESTED");
        return repo.save(b);
    }

    public Booking get(Long id){ return repo.findById(id).orElseThrow(); }
}