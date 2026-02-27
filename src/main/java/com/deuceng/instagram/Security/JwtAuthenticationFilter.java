package com.deuceng.instagram.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Header kontrolü: "Bearer " ile başlamıyorsa devam et (Login/Register olabilir)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token'ı ayıkla ("Bearer " kısmını at)
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 3. Kullanıcı sisteme daha önce kaydedilmemişse doğrula
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Not: İleride burada veritabanından kullanıcıyı kontrol edebilirsin.
            // Şimdilik sadece token'ın geçerliliğine (imzasına) güveniyoruz.
            if (jwtService.isTokenValid(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        null // Şimdilik Roller/Yetkiler boş
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Sisteme "Bu kullanıcı onaylıdır" bilgisini ekle
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}