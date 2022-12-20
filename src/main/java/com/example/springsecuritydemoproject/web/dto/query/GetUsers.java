package com.example.springsecuritydemoproject.web.dto.query;

import com.example.springsecuritydemoproject.utils.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class GetUsers implements Query<GetUsersResult> {

    private String username;
    private String role;

}
