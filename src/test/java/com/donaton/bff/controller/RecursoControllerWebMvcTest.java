package com.donaton.bff.controller;

import com.donaton.bff.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecursoController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecursoControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private JwtService jwtService;

    @Test
    void listarShouldReturn200() throws Exception {
        when(jdbcTemplate.queryForList("SELECT * FROM recursos"))
                .thenReturn(List.of(Map.of("id", 1, "stock", 2)));

        mockMvc.perform(get("/recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listarShouldReturn404WhenServiceThrowsNotFound() throws Exception {
        when(jdbcTemplate.queryForList("SELECT * FROM recursos"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/recursos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarShouldReturn200() throws Exception {
        when(jdbcTemplate.update(eq("UPDATE recursos SET stock = ? WHERE id = ?"), eq(7), eq(1L)))
                .thenReturn(1);

        mockMvc.perform(put("/recursos/1")
                        .contentType(APPLICATION_JSON)
                        .content("{\"stock\":7}"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarShouldReturn404() throws Exception {
        when(jdbcTemplate.update(eq("UPDATE recursos SET stock = ? WHERE id = ?"), eq(7), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/recursos/1")
                        .contentType(APPLICATION_JSON)
                        .content("{\"stock\":7}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarShouldReturn200() throws Exception {
        when(jdbcTemplate.update("DELETE FROM recursos WHERE id = ?", 1L)).thenReturn(1);

        mockMvc.perform(delete("/recursos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarShouldReturn404() throws Exception {
        when(jdbcTemplate.update("DELETE FROM recursos WHERE id = ?", 1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/recursos/1"))
                .andExpect(status().isNotFound());
    }
}
