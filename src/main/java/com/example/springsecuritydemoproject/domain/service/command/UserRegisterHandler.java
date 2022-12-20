package com.example.springsecuritydemoproject.domain.service.command;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.utils.cqrs.Command;
import com.example.springsecuritydemoproject.utils.cqrs.CommandHandler;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.utils.security.jwt.JwtUtils;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.command.UserRegister;
import com.example.springsecuritydemoproject.web.dto.command.UserRegisterResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole.USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRegisterHandler implements CommandHandler<UserRegisterResult, UserRegister> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegisterResult handle(UserRegister command) {
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            log.error("user with email {} is already exists", command.getEmail());
            ApiException.build(HttpStatus.BAD_REQUEST, "user with email \"%s\" is already exists", command.getEmail());
        }

        if (userRepository.findByUsername(command.getUsername()).isPresent()) {
            log.error("user with username {} is already exists", command.getUsername());
            ApiException.build(HttpStatus.BAD_REQUEST, "user with username \"%s\" is already exists", command.getUsername());
        }

        UserEntity userEntity = userRepository.saveAndFlush(
                UserEntity.builder()
                        .email(command.getEmail())
                        .username(command.getUsername())
                        .password(passwordEncoder.encode(command.getPassword()))
                        .createdAt(ZonedDateTime.now())
                        .updatedAt(ZonedDateTime.now())
                        .role(USER)
                        .build());
        log.info("user {} is register", userEntity);

        // TODO: CHECK IF USER IS ALREADY EXIST AND NOT ENABLED THEN THROW ACCOUNT NOT ENABLE EXCEPTION
        // TODO: SEND EMAIL

        return new UserRegisterResult(UserMapper.mapToDto(userEntity));
    }
}
