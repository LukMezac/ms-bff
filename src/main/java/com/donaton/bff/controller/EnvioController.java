package com.donaton.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/envios")
@CrossOrigin("*")
public class EnvioController {

    @Autowired
    private RestTemplate restTemplate;

    // Puerto de tu microservicio logistica
    private final String URL = "http://localhost:8082/envios";

    @GetMapping
    public ResponseEntity<?> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Object e) {
        return restTemplate.postForEntity(URL, e, Object.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Object e) {
        restTemplate.put(URL + "/" + id, e);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        restTemplate.delete(URL + "/" + id);
        return ResponseEntity.ok().build();
    }
}