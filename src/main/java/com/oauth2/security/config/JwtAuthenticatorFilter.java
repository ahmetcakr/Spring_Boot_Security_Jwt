package com.oauth2.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String jwt;
        final String userEmail;
        final String authHeader = request.getHeader("Authorization");
        final String BearerStr = "Bearer ";

        if(authHeader == null || !authHeader.startsWith(BearerStr)){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(BearerStr.length());
        userEmail = jwtService.extractUsername(jwt);
    }
}
