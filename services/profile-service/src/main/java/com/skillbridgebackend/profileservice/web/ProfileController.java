package com.skillbridgebackend.profileservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @GetMapping("/search")
    public List<Map<String,Object>> search(@RequestParam Optional<String> domain,
                                           @RequestParam Optional<String> seniority){
        // integrate DynamoDB SDK (query GSI)
        return List.of(Map.of("name","Alice","domain",domain.orElse("backend"),
                "seniority",seniority.orElse("senior"),
                "badges", List.of("interview coach"), "price",49));
    }
}