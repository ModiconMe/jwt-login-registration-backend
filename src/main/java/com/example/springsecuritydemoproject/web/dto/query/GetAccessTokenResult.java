package com.example.springsecuritydemoproject.web.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetAccessTokenResult {

    private String accessJwtToken;

}
