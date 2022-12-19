package com.example.springsecuritydemoproject.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenService {

    String updateAccessToken(HttpServletRequest request);

}
