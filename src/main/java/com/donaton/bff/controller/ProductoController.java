package com.donaton.bff.controller;

import com.donaton.bff.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/productos")
@CrossOrigin("*")
public class ProductoController {

    @Autowired
    private RestTemplate restTemplate;

    private final String URL = "http://localhost:8081/donaciones";

    // LISTAR - Todos pueden ver (sin token)
    @GetMapping
    public ResponseEntity<?> listar() {
        return restTemplate.getForEntity(URL, Object.class);
    }

    // OBTENER POR ID - Todos pueden ver (sin token)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return restTemplate.getForEntity(URL + "/" + id, Object.class);
    }

    // CREAR - Todos logueados pueden crear (token válido)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Object producto) {
        // El JwtFilter ya validó que hay token
        return restTemplate.postForEntity(URL, producto, Object.class);
    }

    // ACTUALIZAR - Solo ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Object producto) {


        if (!esAdmin()) {
            return ResponseEntity.status(403).body("🔒 Solo admins pueden editar");
        }

        restTemplate.put(URL + "/" + id, producto);
        return ResponseEntity.ok("✅ Producto actualizado");
    }

    //  ELIMINAR - Solo ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {


        if (!esAdmin()) {
            return ResponseEntity.status(403).body("🔒 Solo admins pueden eliminar");
        }

        restTemplate.delete(URL + "/" + id);
        return ResponseEntity.ok("✅ Producto eliminado");
    }

    // Verificar si es admin
    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}