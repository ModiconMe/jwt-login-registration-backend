package com.example.springsecuritydemoproject.web.contoller;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.domain.repository.UserRepository;
import com.example.springsecuritydemoproject.web.dto.command.UserLogin;
import com.example.springsecuritydemoproject.web.dto.command.UserLoginResult;
import com.example.springsecuritydemoproject.web.dto.command.UserRegister;
import com.example.springsecuritydemoproject.web.dto.query.GetUsersResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static String accessToken = "Token ";
    private static String refreshToken = "Token ";

    @Test
    void itShouldRegisterAccount() throws Exception {
        // register account
        UserRegister userRegister = UserRegister.builder()
                .email("user@gmail.com")
                .username("user")
                .password("password")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(userRegister)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userDto.email", Matchers.is(userRegister.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userDto.username", Matchers.is(userRegister.getUsername())));

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userRegister.getEmail());
        assertThat(optionalUser).isPresent();
        optionalUser.get().setEnabled(true);
        userRepository.save(optionalUser.get());

        // login
        UserLogin userLogin = UserLogin.builder()
                .email("user@gmail.com")
                .password("password")
                .build();

        String loginJson = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(userLogin)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userDto.email", Matchers.is(userRegister.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.userDto.username", Matchers.is(userRegister.getUsername())))
                .andReturn().getResponse().getContentAsString();
        UserLoginResult userLoginResult = objectMapper.readValue(loginJson, UserLoginResult.class);
        assertThat(userLoginResult).isNotNull();

        accessToken += userLoginResult.getAccessToken();
        refreshToken += userLoginResult.getRefreshToken();

        // getArticlesByFilter
        String getUsersJson1 = mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .param("username", "user")
                        .param("role", "USER")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        GetUsersResult getUsersResult1 = objectMapper.readValue(getUsersJson1, GetUsersResult.class);
        assertThat(getUsersResult1.getUsers()).isNotEmpty();
        assertThat(getUsersResult1.getUsers().size()).isEqualTo(1);

        String getUsersJson2 = mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        GetUsersResult getUsersResult2 = objectMapper.readValue(getUsersJson2, GetUsersResult.class);
        assertThat(getUsersResult2.getUsers()).isNotEmpty();
        assertThat(getUsersResult2.getUsers().size()).isEqualTo(1);

        String getUsersJson3 = mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .param("username", "user1")
                        .param("role", "USER")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        GetUsersResult getUsersResult3 = objectMapper.readValue(getUsersJson3, GetUsersResult.class);
        assertThat(getUsersResult3.getUsers()).isEmpty();
    }
}