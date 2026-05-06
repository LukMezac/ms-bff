package com.donaton.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final RestTemplate restTemplate;
    // URL configurada según tu test para el microservicio de donaciones
    private static final String URL = "http://localhost:8081/donaciones";

    public ProductoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(@PathVariable String id) {
        // Soporta el test obtenerShouldReturn200 y 404
        return restTemplate.getForEntity(URL + "/" + id, Object.class);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Object producto) {
        return restTemplate.postForEntity(URL, producto, Object.class);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizar(@PathVariable String id, @RequestBody Object producto) {
        // Si el microservicio responde 404, RestTemplate lanza HttpClientErrorException
        // El GlobalExceptionHandler (RestControllerAdvice) debe capturarlo
        restTemplate.put(URL + "/" + id, producto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        // Valida los tests de eliminar con rol ADMIN y respuesta 404
        restTemplate.delete(URL + "/" + id);
        return ResponseEntity.ok().build();
    }
}