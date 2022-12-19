package com.example.springsecuritydemoproject.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("user")
public class UserDto {

    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String bio;
    private String image;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
