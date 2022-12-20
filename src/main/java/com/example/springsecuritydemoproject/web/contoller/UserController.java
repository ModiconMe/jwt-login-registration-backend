package com.example.springsecuritydemoproject.web.contoller;

import com.example.springsecuritydemoproject.utils.cqrs.Bus;
import com.example.springsecuritydemoproject.web.dto.command.UserLogin;
import com.example.springsecuritydemoproject.web.dto.command.UserLoginResult;
import com.example.springsecuritydemoproject.web.dto.command.UserRegister;
import com.example.springsecuritydemoproject.web.dto.command.UserRegisterResult;
import com.example.springsecuritydemoproject.web.dto.query.GetAccessToken;
import com.example.springsecuritydemoproject.web.dto.query.GetAccessTokenResult;
import com.example.springsecuritydemoproject.web.dto.query.GetUsers;
import com.example.springsecuritydemoproject.web.dto.query.GetUsersResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final Bus bus;

    @PostMapping
    public UserRegisterResult registry(@RequestBody @Valid UserRegister request) {
        return bus.executeCommand(request);
    }

    @PostMapping("/login")
    public UserLoginResult login(@RequestBody @Valid UserLogin request) {
        return bus.executeCommand(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public GetUsersResult getUserByUsername(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "role", required = false) String role
    ) {
        return bus.executeQuery(GetUsers.builder().username(username).role(role).build());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String onlyAdmin() {
        return "Admin";
    }

    @GetMapping("/refresh")
    public GetAccessTokenResult updateAccessToken(HttpServletRequest request) {
        String refreshJwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return bus.executeQuery(new GetAccessToken(refreshJwtToken));
    }

}