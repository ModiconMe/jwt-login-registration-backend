package com.example.springsecuritydemoproject.web.dto.command;

import com.example.springsecuritydemoproject.web.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonRootName("user")
public class UserLoginResult {

    private UserDto userDto;
    private String accessToken;
    private String refreshToken;

}
