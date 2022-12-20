package com.example.springsecuritydemoproject.utils.cqrs;

public interface CommandHandler<R, C extends Command<R>>{

    R handle(C command);

}
