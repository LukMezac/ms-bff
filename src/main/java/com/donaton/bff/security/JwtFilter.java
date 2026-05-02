package com.donaton.bff.security;

import com.donaton.bff.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter implements Filter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String method = req.getMethod();

        // 🔥 CORS
        if (method.equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // 🔓 LOGIN LIBRE
        if (uri.contains("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // 🔓 GET público
        if (method.equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

        // 🔒 TODO LO DEMÁS requiere token

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("❌ Token requerido");
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            String username = jwtService.extractUsername(token);

            List<String> roles = jwtService.extractRoles(token);
            if (roles == null) roles = new ArrayList<>();

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

            // 🔒 SOLO ADMIN para PUT y DELETE
            if (method.equals("PUT") || method.equals("DELETE")) {
                boolean esAdmin = authorities.stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (!esAdmin) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("🚫 Solo admin puede modificar");
                    return;
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("🔒 Token inválido");
        }
    }
}