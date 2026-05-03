package com.donaton.bff.controller;

import com.donaton.bff.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class AuthController {

    private final JwtService jwtService;
    private final JdbcTemplate jdbcTemplate;

    public AuthController(JwtService jwtService, JdbcTemplate jdbcTemplate) {
        this.jwtService = jwtService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // =========================
    //  DTO LOGIN
    // =========================
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // =========================
    // DTO REGISTRO
    // =========================
    public static class RegistroRequest {
        private String usuario;
        private String email;
        private String password;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // =========================
    // 🔐 LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {

            //  Buscar usuario por email
            String sql = "SELECT nombre, rol, password FROM usuarios WHERE email = ?";
            List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql, request.getUsername());

            //  Usuario no existe
            if (resultados.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
            }

            Map<String, Object> usuario = resultados.get(0);

            String dbPassword = (String) usuario.get("password");

            // Password incorrecta
            if (!dbPassword.equals(request.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Clave incorrecta"));
            }

            // Obtener rol y formatearlo
            String rolDb = (String) usuario.get("rol");
            String rolFinal = (rolDb != null) ? rolDb.trim().toUpperCase() : "USER";

            // IMPORTANTE: ROLE_*
            List<String> roles = List.of("ROLE_" + rolFinal);

            // Generar token
            String token = jwtService.generarToken(request.getUsername(), roles);

            // Respuesta completa
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "rol", rolFinal,
                    "nombre", usuario.get("nombre") != null ? usuario.get("nombre") : "Usuario"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error en login"));
        }
    }

    // =========================
    // REGISTRO
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest request) {
        try {

            // Verificar si existe
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, request.getEmail());

            if (count != null && count > 0) {
                return ResponseEntity.status(400).body(Map.of("error", "El email ya está registrado"));
            }

            // Insertar usuario
            String insertSql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, 'USER')";
            jdbcTemplate.update(insertSql,
                    request.getUsuario(),
                    request.getEmail(),
                    request.getPassword()
            );

            return ResponseEntity.ok(Map.of("mensaje", "Cuenta creada correctamente"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al registrar usuario"));
        }
    }
}