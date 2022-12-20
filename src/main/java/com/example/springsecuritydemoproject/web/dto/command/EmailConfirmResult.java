package com.example.springsecuritydemoproject.web.dto.command;

import com.example.springsecuritydemoproject.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailConfirmResult {

    private UserDto user;

}
