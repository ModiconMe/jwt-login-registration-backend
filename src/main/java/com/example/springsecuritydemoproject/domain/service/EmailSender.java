package com.example.springsecuritydemoproject.domain.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailSender {

    void send(String to, String email);

}
