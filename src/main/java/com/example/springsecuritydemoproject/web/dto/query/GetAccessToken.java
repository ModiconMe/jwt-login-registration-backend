package com.example.springsecuritydemoproject.web.dto.query;

import com.example.springsecuritydemoproject.utils.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetAccessToken implements Query<GetAccessTokenResult> {

    private String refreshJwtToken;

}
