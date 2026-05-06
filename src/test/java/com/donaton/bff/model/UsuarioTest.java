package com.donaton.bff.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioTest {

    @Test
    void shouldSetAndGetAllFields() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@mail.com");
        usuario.setPassword("1234");
        usuario.setRol("ADMIN");

        assertEquals(10L, usuario.getId());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("juan@mail.com", usuario.getEmail());
        assertEquals("1234", usuario.getPassword());
        assertEquals("ADMIN", usuario.getRol());
    }
}
