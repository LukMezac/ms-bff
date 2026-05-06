package com.donaton.bff.controller;

import com.donaton.bff.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NecesidadController.class)
@AutoConfigureMockMvc(addFilters = false)
class NecesidadControllerWebMvcTest {

    private static final String URL = "http://localhost:8080/necesidades";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private JwtService jwtService;

    @Test
    void listarShouldReturn200() throws Exception {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/necesidades"))
                .andExpect(status().isOk());
    }

    @Test
    void listarShouldReturn404() throws Exception {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(get("/necesidades"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearShouldReturn200AndForwardToken() throws Exception {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/necesidades")
                        .header("Authorization", "Bearer abc")
                        .contentType(APPLICATION_JSON)
                        .content("{\"a\":1}"))
                .andExpect(status().isOk());

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq(URL), eq(HttpMethod.POST), captor.capture(), eq(Object.class));
        assertEquals("Bearer abc", captor.getValue().getHeaders().getFirst("Authorization"));
    }

    @Test
    void actualizarShouldReturn200() throws Exception {
        when(restTemplate.exchange(eq(URL + "/1"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/necesidades/1")
                        .header("Authorization", "Bearer abc")
                        .contentType(APPLICATION_JSON)
                        .content("{\"a\":2}"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarShouldReturn404() throws Exception {
        when(restTemplate.exchange(eq(URL + "/1"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(put("/necesidades/1")
                        .contentType(APPLICATION_JSON)
                        .content("{\"a\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarShouldReturn200() throws Exception {
        when(restTemplate.exchange(eq(URL + "/1"), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/necesidades/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarShouldReturn404() throws Exception {
        when(restTemplate.exchange(eq(URL + "/1"), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(delete("/necesidades/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarShouldSendNoAuthorizationHeaderWhenMissing() throws Exception {
        when(restTemplate.exchange(eq(URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/necesidades"))
                .andExpect(status().isOk());

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq(URL), eq(HttpMethod.GET), captor.capture(), eq(Object.class));
        assertNull(captor.getValue().getHeaders().getFirst("Authorization"));
    }
}
