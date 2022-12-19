package com.example.springsecuritydemoproject.dto;

import com.example.springsecuritydemoproject.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static UserDto mapToDto(UserEntity userEntity, String token) {

        return UserDto.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .token(token)
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

}
