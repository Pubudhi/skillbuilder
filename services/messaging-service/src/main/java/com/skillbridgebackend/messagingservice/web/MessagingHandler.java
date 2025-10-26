package com.skillbridgebackend.messagingservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Component
public class MessagingHandler {
    @Bean
    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/api/messages/stream", this::stream)
                .POST("/api/messages", this::post)
                .build();
    }

    Mono<ServerResponse> stream(ServerRequest r){
        Flux<String> ticks = Flux.interval(Duration.ofSeconds(1))
                .map(i -> "tick " + i).take(5);
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(ticks, String.class);
    }

    Mono<ServerResponse> post(ServerRequest r){
        return r.bodyToMono(String.class).flatMap(b ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("{\"ok\":true}")
        );
    }
}