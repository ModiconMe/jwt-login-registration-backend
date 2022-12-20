package com.example.springsecuritydemoproject.domain.service.command;

import com.example.springsecuritydemoproject.utils.cqrs.CommandHandler;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.command.UserLogin;
import com.example.springsecuritydemoproject.web.dto.command.UserLoginResult;
import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.utils.security.AppUserDetails;
import com.example.springsecuritydemoproject.utils.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserLoginHandler implements CommandHandler<UserLoginResult, UserLogin> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserLoginResult handle(UserLogin command) {
        UserEntity user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> ApiException.build(HttpStatus.NOT_FOUND,
                        "user with email \"%s\" is not found", command.getEmail()));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            log.error("user with password {} is not found", command.getPassword());
            ApiException.build(HttpStatus.NOT_FOUND, "user with password \"%s\" is not found", command.getPassword());
        }

        AppUserDetails userDetails = AppUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().getGrantedAuthorities())
                .build();

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        log.info("user with email {} login", user.getEmail());

        return new UserLoginResult(UserMapper.mapToDto(user), accessToken, refreshToken);
    }

}
