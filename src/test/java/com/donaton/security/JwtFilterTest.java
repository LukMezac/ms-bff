package com.donaton.security;

import com.donaton.bff.security.JwtFilter;
import com.donaton.bff.service.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtFilterTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final JwtFilter jwtFilter = new JwtFilter(jwtService);
    private final FilterChain chain = mock(FilterChain.class);

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAllowOptionsRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/productos");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldAllowAuthEndpointsWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldAllowGetWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/productos");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldReturn401WhenTokenIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/productos");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
    }

    @Test
    void shouldReturn403WhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/productos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(jwtService.extractUsername("token")).thenThrow(new RuntimeException("invalid"));

        jwtFilter.doFilter(request, response, chain);

        assertEquals(403, response.getStatus());
    }

    @Test
    void shouldReturn403ForPutWithoutAdminRole() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/productos/1");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token")).thenReturn("ana");
        when(jwtService.extractRoles("token")).thenReturn(List.of("ROLE_USER"));

        jwtFilter.doFilter(request, response, chain);

        assertEquals(403, response.getStatus());
    }

    @Test
    void shouldAllowPutWithAdminRole() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/productos/1");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token")).thenReturn("ana");
        when(jwtService.extractRoles("token")).thenReturn(List.of("ROLE_ADMIN"));

        jwtFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldAllowPostWithUserRole() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/productos");
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token")).thenReturn("ana");
        when(jwtService.extractRoles("token")).thenReturn(List.of("ROLE_USER"));

        jwtFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
