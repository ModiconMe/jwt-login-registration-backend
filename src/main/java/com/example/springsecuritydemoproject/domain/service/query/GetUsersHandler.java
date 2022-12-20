package com.example.springsecuritydemoproject.domain.service.query;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.utils.cqrs.QueryHandler;
import com.example.springsecuritydemoproject.web.dto.UserMapper;
import com.example.springsecuritydemoproject.web.dto.query.GetUsers;
import com.example.springsecuritydemoproject.web.dto.query.GetUsersResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetUsersHandler implements QueryHandler<GetUsersResult, GetUsers> {

    private final UserRepository userRepository;

    @Override
    public GetUsersResult handle(GetUsers query) {

        List<UserEntity> users = userRepository.findByFilter(query.getUsername(), query.getRole());

        return new GetUsersResult(users.stream()
                .map(UserMapper::mapToDto)
                .toList());
    }
}
