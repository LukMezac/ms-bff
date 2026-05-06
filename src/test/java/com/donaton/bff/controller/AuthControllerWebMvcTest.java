package com.donaton.bff.controller;

import com.donaton.bff.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void loginShouldReturn200WhenCredentialsAreValid() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), eq("user@mail.com")))
                .thenReturn(List.of(Map.of("nombre", "Ana", "rol", "user", "password", "1234")));
        when(jwtService.generarToken("user@mail.com", List.of("ROLE_USER")))
                .thenReturn("token-123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"))
                .andExpect(jsonPath("$.rol").value("USER"))
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void loginShouldReturn401WhenUserDoesNotExist() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), eq("user@mail.com")))
                .thenReturn(List.of());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado"));
    }

    @Test
    void loginShouldReturn401WhenPasswordIsInvalid() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), eq("user@mail.com")))
                .thenReturn(List.of(Map.of("nombre", "Ana", "rol", "USER", "password", "xxxx")));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Clave incorrecta"));
    }

    @Test
    void loginShouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
        doThrow(new RuntimeException("db error")).when(jdbcTemplate).queryForList(anyString(), eq("user@mail.com"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error en login"));
    }

    @Test
    void registerShouldReturn200WhenEmailIsNew() throws Exception {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("new@mail.com"))).thenReturn(0);
        when(jdbcTemplate.update(anyString(), eq("Ana"), eq("new@mail.com"), eq("1234"))).thenReturn(1);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"Ana\",\"email\":\"new@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cuenta creada correctamente"));
    }

    @Test
    void registerShouldReturn400WhenEmailAlreadyExists() throws Exception {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("used@mail.com"))).thenReturn(1);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"Ana\",\"email\":\"used@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El email ya está registrado"));
    }

    @Test
    void registerShouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
        doThrow(new RuntimeException("db error")).when(jdbcTemplate)
                .queryForObject(anyString(), eq(Integer.class), eq("fail@mail.com"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"Ana\",\"email\":\"fail@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error al registrar usuario"));
    }
}
