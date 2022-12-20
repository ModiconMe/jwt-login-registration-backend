package com.example.springsecuritydemoproject.web.dto;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static UserDto mapToDto(UserEntity userEntity) {

        return UserDto.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

}
