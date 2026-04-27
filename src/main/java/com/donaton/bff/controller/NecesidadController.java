package com.donaton.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/necesidades")
@CrossOrigin("*")
public class NecesidadController {

    @Autowired
    private RestTemplate restTemplate;

    // ⚠️ AJUSTA ESTE PUERTO SEGÚN TU MS
    private final String URL = "http://localhost:8080/necesidades";

    @GetMapping
    public ResponseEntity<?> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Object necesidad) {
        return restTemplate.postForEntity(URL, necesidad, Object.class);
    }
}