package com.example.springsecuritydemoproject.service;

import com.example.springsecuritydemoproject.dto.UserDto;
import com.example.springsecuritydemoproject.dto.UserLoginRequest;
import com.example.springsecuritydemoproject.dto.UserMapper;
import com.example.springsecuritydemoproject.dto.UserRegisterRequest;
import com.example.springsecuritydemoproject.entity.UserEntity;
import com.example.springsecuritydemoproject.exception.ApiException;
import com.example.springsecuritydemoproject.repository.UserRepository;
import com.example.springsecuritydemoproject.security.AppUserDetails;
import com.example.springsecuritydemoproject.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static com.example.springsecuritydemoproject.security.roles.ApplicationUserRole.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserDto registry(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            ApiException.build(HttpStatus.BAD_REQUEST, "user with email \"%s\" is already exists", request.getEmail());

        if (userRepository.findByUsername(request.getUsername()).isPresent())
                ApiException.build(HttpStatus.BAD_REQUEST, "user with username \"%s\" is already exists", request.getUsername());

        UserEntity userEntity = userRepository.saveAndFlush(
                UserEntity.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .createdAt(ZonedDateTime.now())
                        .updatedAt(ZonedDateTime.now())
                        .role(USER)
                        .build());

        return UserMapper.mapToDto(userEntity, "");
    }

    @Override
    public UserDto login(UserLoginRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> ApiException.build(
                        HttpStatus.NOT_FOUND,
                        "user with email \"%s\" is not found", request.getEmail())
                );

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword()))
            ApiException.build(HttpStatus.NOT_FOUND, "user with password \"%s\" is not found", request.getPassword());

        AppUserDetails accountDetails = AppUserDetails.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole().getGrantedAuthorities())
                .build();

        return UserMapper.mapToDto(userEntity, jwtUtils.generateAccessToken(accountDetails));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(u -> UserMapper.mapToDto(u, ""))
                .toList();
    }

}
