package com.example.springsecuritydemoproject.web.dto.query;

import com.example.springsecuritydemoproject.utils.cqrs.Query;
import com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetUsers implements Query<GetUsersResult> {

    private String username;
    private String role;

}
