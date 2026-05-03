package com.donaton.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/necesidades")
@CrossOrigin("origins = \"http://127.0.0.1:8090\", allowedHeaders = \"*\", allowCredentials = \"true\"")
public class NecesidadController {

    @Autowired
    private RestTemplate restTemplate;

    // Ajusta el puerto a 8080 u 8090 según corresponda a tu microservicio de necesidades
    private final String URL = "http://localhost:8080/necesidades";


    private HttpHeaders createHeaders(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authHeader != null && !authHeader.isEmpty()) {
            headers.set("Authorization", authHeader);
        }
        return headers;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader(value = "Authorization", required = false) String token) {
        HttpEntity<Void> request = new HttpEntity<>(createHeaders(token));
        return restTemplate.exchange(URL, HttpMethod.GET, request, Object.class);
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Object n,
            @RequestHeader(value = "Authorization", required = false) String token) {
        HttpEntity<Object> request = new HttpEntity<>(n, createHeaders(token));
        return restTemplate.exchange(URL, HttpMethod.POST, request, Object.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Object n,
            @RequestHeader(value = "Authorization", required = false) String token) {
        HttpEntity<Object> request = new HttpEntity<>(n, createHeaders(token));
        return restTemplate.exchange(URL + "/" + id, HttpMethod.PUT, request, Object.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        HttpEntity<Void> request = new HttpEntity<>(createHeaders(token));
        return restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, request, Object.class);
    }
}