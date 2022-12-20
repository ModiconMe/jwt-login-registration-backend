package com.example.springsecuritydemoproject.domain.service.query;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.utils.cqrs.QueryHandler;
import com.example.springsecuritydemoproject.utils.exception.ApiException;
import com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.query.GetUsers;
import com.example.springsecuritydemoproject.web.dto.query.GetUsersResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetUsersHandler implements QueryHandler<GetUsersResult, GetUsers> {

    private final UserRepository userRepository;

    @Override
    public GetUsersResult handle(GetUsers query) {

        ApplicationUserRole role = null;

        Optional<ApplicationUserRole> optionalRole = Arrays.stream(ApplicationUserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(query.getRole())).findFirst();

        if (optionalRole.isEmpty() && query.getRole() != null)
            ApiException.build(HttpStatus.BAD_REQUEST, "Role \"s%\" is not exist", query.getRole());

        if (query.getRole() != null)
            role = optionalRole.get();

        List<UserEntity> users = userRepository.findByFilter(query.getUsername(), role);
        return new GetUsersResult(users.stream()
                .map(UserMapper::mapToDto)
                .toList());
    }
}
