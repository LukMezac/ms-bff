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

    private final String URL = "http://localhost:8080/necesidades";

    @GetMapping
    public ResponseEntity<?> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Object n) {
        return restTemplate.postForEntity(URL, n, Object.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Object n) {
        restTemplate.put(URL + "/" + id, n);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        restTemplate.delete(URL + "/" + id);
        return ResponseEntity.ok().build();
    }
}