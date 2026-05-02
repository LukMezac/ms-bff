package com.donaton.bff.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recursos") // <--- ESTO arregla el 404
@CrossOrigin(origins = "http://localhost:3000") // Permite que Next.js se conecte
public class RecursoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> listarRecursos() {
        return jdbcTemplate.queryForList("SELECT * FROM recursos");
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        jdbcTemplate.update("DELETE FROM recursos WHERE id = ?", id);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        jdbcTemplate.update("UPDATE recursos SET stock = ? WHERE id = ?",
                data.get("stock"), id);
    }
}