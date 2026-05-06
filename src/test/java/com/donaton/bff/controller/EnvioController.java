package com.donaton.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final RestTemplate restTemplate;
    // URL del microservicio de logística/envíos según tu arquitectura BFF
    private static final String URL = "http://localhost:8082/envios";

    public EnvioController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<Object> listar() {
        // Delega la petición al microservicio y retorna la respuesta directa
        return restTemplate.getForEntity(URL, Object.class);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Object envio) {
        // Realiza el POST al microservicio remoto
        return restTemplate.postForEntity(URL, envio, Object.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable String id, @RequestBody Object envio) {
        // RestTemplate lanzará HttpClientErrorException si el ID no existe (404)
        // El GlobalExceptionHandler capturará esa excepción para que el test pase
        restTemplate.put(URL + "/" + id, envio);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        // Al igual que en Put, delegamos la gestión del error al manejador global
        restTemplate.delete(URL + "/" + id);
        return ResponseEntity.ok().build();
    }
}