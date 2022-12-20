package com.example.springsecuritydemoproject.web.dto.command;

import com.example.springsecuritydemoproject.utils.cqrs.Command;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("user")
public class UserRegister implements Command<UserRegisterResult> {

    @NotBlank(message = "User name should be not blank")
    @Pattern(regexp = "[\\w\\d]{1,30}", message = "string contains alphabet or digit with length 1 to 30")
    private String username;

    @NotBlank(message = "Email should be not blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should be not blank")
    @Size(min = 8, max = 32, message = "Password should be above 8 and less then 32 characters")
    private String password;

}
