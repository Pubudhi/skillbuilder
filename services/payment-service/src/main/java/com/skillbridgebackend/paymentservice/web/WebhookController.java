package com.skillbridgebackend.paymentservice.web;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pay")
public class WebhookController {

    @Value("${stripe.webhook.secret}") String endpointSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handle(@RequestHeader("Stripe-Signature") String sigHeader,
                                         @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            switch (event.getType()) {
                case "checkout.session.completed" -> {
                    var session = (com.stripe.model.checkout.Session) event.getDataObjectDeserializer()
                            .getObject().orElseThrow();
                    String bookingId = session.getMetadata().get("bookingId");
                    // TODO: mark booking as PAID in Aurora (call Booking service or update DB)
                }
                default -> {}
            }
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("invalid signature");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("error");
        }
    }
}