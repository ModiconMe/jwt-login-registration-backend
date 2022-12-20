package com.example.springsecuritydemoproject.domain.service.query;

import com.example.springsecuritydemoproject.utils.cqrs.QueryHandler;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.utils.security.jwt.JwtConfig;
import com.example.springsecuritydemoproject.utils.security.jwt.JwtUtils;
import com.example.springsecuritydemoproject.web.dto.query.GetAccessToken;
import com.example.springsecuritydemoproject.web.dto.query.GetAccessTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetAccessTokenHandler implements QueryHandler<GetAccessTokenResult, GetAccessToken> {

    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    public GetAccessTokenResult handle(GetAccessToken query) {
        return Optional.ofNullable(query.getRefreshJwtToken())
                .filter(authHeader -> authHeader.startsWith(jwtConfig.getTokenPrefix()))
                .map(authHeader -> authHeader.substring(jwtConfig.getTokenPrefix().length()))
                .filter(jwtUtils::isTokenValid)
                .map(jwtUtils::extractUsername)
                .map(userDetailsService::loadUserByUsername)
                .map(jwtUtils::generateAccessToken)
                .map(GetAccessTokenResult::new)
                .orElseThrow(() -> ApiException.build(HttpStatus.BAD_REQUEST, "unable to generate access token with such refresh token"));
    }

}
