package com.donaton.bff.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class BffController {
    // integración BFF funcionando correctamente
    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 DONACIONES
    @GetMapping("/donaciones")
    public Object obtenerDonaciones() {
        return restTemplate.getForObject(
                "http://localhost:8081/donaciones",
                Object.class
        );
    }

    // 🔹 NECESIDADES
    @GetMapping("/necesidades")
    public Object obtenerNecesidades() {
        return restTemplate.getForObject(
                "http://localhost:8080/necesidades",
                Object.class
        );
    }
}