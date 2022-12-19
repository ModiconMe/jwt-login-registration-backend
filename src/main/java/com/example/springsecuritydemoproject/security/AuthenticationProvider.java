package com.example.springsecuritydemoproject.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Authenticate user by jwt token (username from token)
 */
@Component
@RequiredArgsConstructor
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public Authentication getAuthentication(String username) {
        UserDetails userDetails1 = userDetailsService.loadUserByUsername(username);
        return Optional.ofNullable(username)
                .map(userDetailsService::loadUserByUsername)
                .map(userDetails ->
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()))
                .orElse(null);
    }

}