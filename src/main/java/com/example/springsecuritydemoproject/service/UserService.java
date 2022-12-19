package com.example.springsecuritydemoproject.service;

import com.example.springsecuritydemoproject.dto.UserDto;
import com.example.springsecuritydemoproject.dto.UserLoginRequest;
import com.example.springsecuritydemoproject.dto.UserRegisterRequest;

import java.util.List;

public interface UserService {

    UserDto registry(UserRegisterRequest request);

    UserDto login(UserLoginRequest request);

    List<UserDto> getUsers();

}
