package com.example.springsecuritydemoproject.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {

    private String username;
    private String email;
    private String bio;
    private String image;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
