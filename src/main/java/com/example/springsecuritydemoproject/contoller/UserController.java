package com.example.springsecuritydemoproject.contoller;

import com.example.springsecuritydemoproject.dto.UserDto;
import com.example.springsecuritydemoproject.dto.UserLoginRequest;
import com.example.springsecuritydemoproject.dto.UserRegisterRequest;
import com.example.springsecuritydemoproject.security.jwt.JwtTokenService;
import com.example.springsecuritydemoproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService accountService;
    private final JwtTokenService jwtTokenService;

    @PostMapping
    public UserDto registry(@RequestBody @Valid UserRegisterRequest request) {
        return accountService.registry(request);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody @Valid UserLoginRequest request) {
        return accountService.login(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<UserDto> getUsers() {
        return accountService.getUsers();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String onlyAdmin() {
        return "Admin";
    }

    @GetMapping("/refresh")
    public String updateAccessToken(HttpServletRequest request) {
        return jwtTokenService.updateAccessToken(request);
    }

}