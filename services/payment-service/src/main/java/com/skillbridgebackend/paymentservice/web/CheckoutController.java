package com.skillbridgebackend.paymentservice.web;

import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

record CreateCheckoutRequest(@NotNull String bookingId, @NotNull Long amountCents, @NotNull String currency) {}

@RestController
@RequestMapping("/api/pay")
public class CheckoutController {

    @Value("${stripe.secret}") String stripeSecret;
    @Value("${app.url.success}") String successUrl;
    @Value("${app.url.cancel}") String cancelUrl;

    @PostConstruct void init(){ Stripe.apiKey = stripeSecret; }

    @PostMapping("/checkout")
    public ResponseEntity<?> create(@RequestBody CreateCheckoutRequest req) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(req.currency())
                                .setUnitAmount(req.amountCents())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("SkillBridge Booking " + req.bookingId()).build())
                                .build())
                        .build())
                .putMetadata("bookingId", req.bookingId())
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .build();
        Session session = Session.create(params);
        return ResponseEntity.ok().body(session);
    }
}