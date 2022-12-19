package com.example.springsecuritydemoproject.security.jwt;

import com.example.springsecuritydemoproject.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    public String updateAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(jwtConfig.getTokenPrefix()))
                .map(authHeader -> authHeader.substring(jwtConfig.getTokenPrefix().length()))
                .filter(jwtUtils::isTokenValid)
                .map(jwtUtils::extractUsername)
                .map(userDetailsService::loadUserByUsername)
                .map(jwtUtils::generateAccessToken)
                .orElseThrow(() -> ApiException.build(HttpStatus.BAD_REQUEST, "unable to generate access token with such refresh token"));

    }

}
