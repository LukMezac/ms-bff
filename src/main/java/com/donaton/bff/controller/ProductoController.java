package com.donaton.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/productos")
@CrossOrigin("*")
public class ProductoController {

    @Autowired
    private RestTemplate restTemplate;

    private final String URL = "http://localhost:8081/donaciones";

    @GetMapping
    public ResponseEntity<?> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Object producto) {
        return restTemplate.postForEntity(URL, producto, Object.class);
    }
}