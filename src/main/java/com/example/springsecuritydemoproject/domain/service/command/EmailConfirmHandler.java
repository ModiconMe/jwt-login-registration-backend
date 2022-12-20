package com.example.springsecuritydemoproject.domain.service.command;

import com.example.springsecuritydemoproject.domain.entity.ConfirmationToken;
import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.ConfirmationTokenRepository;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.utils.cqrs.CommandHandler;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.command.EmailConfirm;
import com.example.springsecuritydemoproject.web.dto.command.EmailConfirmResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailConfirmHandler implements CommandHandler<EmailConfirmResult, EmailConfirm> {

    private final UserRepository userRepo;
    private final ConfirmationTokenRepository tokenRepo;

    @Override
    @Transactional
    public EmailConfirmResult handle(EmailConfirm command) {
        Optional<ConfirmationToken> optionalToken = tokenRepo.findByToken(command.getConfirmationToken());
        if (optionalToken.isEmpty()) {
            log.error("token with uuid {} is not found", command.getConfirmationToken());
            ApiException.build(HttpStatus.NOT_FOUND, "token with uuid \"%s\" is not found", command.getConfirmationToken());
        }

        ConfirmationToken confirmationToken = optionalToken.get();

        UserEntity user = confirmationToken.getUserEntity();
        if (user == null) {
            log.error("token user is not found");
            ApiException.build(HttpStatus.NOT_FOUND, "token user is not found");
        }

        if (confirmationToken.getConfirmedAt() != null) {
            log.error("email {} is already confirmed", user.getEmail());
            ApiException.build(HttpStatus.BAD_REQUEST, "email \"%s\" is already confirmed", user.getEmail());
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            log.error("token is expired");
            ApiException.build(HttpStatus.BAD_REQUEST, "token is expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        user.setEnabled(true);

        tokenRepo.save(confirmationToken);

        return new EmailConfirmResult(UserMapper.mapToDto(user));
    }

}
