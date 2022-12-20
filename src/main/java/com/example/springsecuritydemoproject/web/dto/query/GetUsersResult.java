package com.example.springsecuritydemoproject.web.dto.query;

import com.example.springsecuritydemoproject.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetUsersResult {

    private List<UserDto> users;

}
