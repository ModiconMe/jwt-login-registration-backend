package com.example.springsecuritydemoproject.web.dto.command;

import com.example.springsecuritydemoproject.utils.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmailConfirm implements Command<EmailConfirmResult> {

    private String confirmationToken;

}
