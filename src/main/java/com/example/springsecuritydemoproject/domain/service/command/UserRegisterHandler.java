package com.example.springsecuritydemoproject.domain.service.command;

import com.example.springsecuritydemoproject.domain.entity.ConfirmationToken;
import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.ConfirmationTokenRepository;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.domain.service.EmailSender;
import com.example.springsecuritydemoproject.domain.service.EmailTextBuilder;
import com.example.springsecuritydemoproject.utils.cqrs.CommandHandler;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.command.UserRegister;
import com.example.springsecuritydemoproject.web.dto.command.UserRegisterResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole.USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRegisterHandler implements CommandHandler<UserRegisterResult, UserRegister> {

    private final UserRepository userRepo;
    private final ConfirmationTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final EmailTextBuilder emailTextBuilder;

    @Override
    @Transactional
    public UserRegisterResult handle(UserRegister command) {
        // check that user is not exist by email and, if exists, check activated
        Optional<UserEntity> byEmail = userRepo.findByEmail(command.getEmail());
        if (byEmail.isPresent()) {
            if (!byEmail.get().isEnabled()) {
                log.error("user with email {} is not activated", command.getEmail());
                ApiException.build(HttpStatus.BAD_REQUEST, "user with email \"%s\" is not activated", command.getEmail());
            }
            log.error("user with email {} is already exists", command.getEmail());
            ApiException.build(HttpStatus.BAD_REQUEST, "user with email \"%s\" is already exists", command.getEmail());
        }

        // check that user is not exist by username and, if exists, check activated
        Optional<UserEntity> byUsername = userRepo.findByUsername(command.getUsername());
        if (byUsername.isPresent()) {
            if (!byUsername.get().isEnabled()) {
                log.error("user with username {} is not activated", command.getUsername());
                ApiException.build(HttpStatus.BAD_REQUEST, "user with username \"%s\" is not activated", command.getEmail());
            }
            log.error("user with username {} is already exists", command.getUsername());
            ApiException.build(HttpStatus.BAD_REQUEST, "user with username \"%s\" is already exists", command.getUsername());
        }

        UserEntity user = userRepo.saveAndFlush(
                UserEntity.builder()
                        .email(command.getEmail())
                        .username(command.getUsername())
                        .password(passwordEncoder.encode(command.getPassword()))
                        .createdAt(ZonedDateTime.now())
                        .updatedAt(ZonedDateTime.now())
                        .role(USER)
                        .build());

        ConfirmationToken confirmationToken = tokenRepo.saveAndFlush(ConfirmationToken.builder()
                .userEntity(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build());

        log.info("user {} is register", user);

        String link = "http://localhost:8080/api/users/confirm?token=" + confirmationToken.getToken();
        emailSender.send(
                user.getEmail(),
                emailTextBuilder.buildEmail(user.getUsername(), link)
        );
        log.info("token {} is created and send", confirmationToken.getToken());

        return new UserRegisterResult(UserMapper.mapToDto(user));
    }
}
